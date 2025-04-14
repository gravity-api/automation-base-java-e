package io.automation.engine.core;

import io.automation.engine.models.AutomationEnvironment;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.CookieManager;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the model for setting up automation objects.
 */
public class TestContext {
    // Logger instance for logging messages.
    private static final Logger log = LoggerFactory.getLogger(TestContext.class);

    /**
     * The HTTP client instance is created for making HTTP requests.
     * It follows redirects and uses a cookie manager for handling cookies.
     */
    public static final HttpClient HTTP_CLIENT = HttpClient
            .newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .cookieHandler(new CookieManager())
            .build();

    // The automation environment containing context properties and configurations.
    private AutomationEnvironment environment;

    // A map of WebDriver instances, where the key is the driver name.
    private Map<String, WebDriver> drivers;

    // The primary WebDriver instance used in the setup.
    private WebDriver webDriver;

    /**
     * Default constructor for the `TestContext` class.
     * Initializes the automation environment and the drivers map.
     */
    public TestContext() {
        this(new AutomationEnvironment());
    }

    /**
     * Constructs a new `TestContext` instance with the specified automation environment.
     * This constructor initializes the environment and prepares the map for storing WebDriver instances.
     *
     * @param environment the automation environment to associate with this test context.
     */
    public TestContext(AutomationEnvironment environment) {
        // Set the provided automation environment instance.
        this.environment = environment;

        // Create a new HashMap to store WebDriver instances by their names.
        drivers = new HashMap<>();
    }

    /**
     * Gets the automation context for the setup.
     *
     * @return the automation environment.
     */
    public AutomationEnvironment getEnvironment() {
        return environment;
    }

    /**
     * Sets the automation context for the setup.
     *
     * @param environment the automation environment to set.
     */
    public void setEnvironment(AutomationEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Gets the dictionary of web drivers used in the setup.
     * The keys are driver names and the values are instances of {@link WebDriver}.
     *
     * @return a map of driver names to {@link WebDriver} instances.
     */
    public Map<String, WebDriver> getDrivers() {
        return drivers;
    }

    /**
     * Sets the dictionary of web drivers used in the setup.
     * The keys are driver names and the values are instances of {@link WebDriver}.
     *
     * @param drivers the map of drivers to set.
     */
    public void setDrivers(Map<String, WebDriver> drivers) {
        this.drivers = drivers;
    }

    /**
     * Gets the primary web driver used in the setup.
     *
     * @return the primary {@link WebDriver}.
     */
    public WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * Sets the primary web driver used in the setup.
     *
     * @param webDriver the primary {@link WebDriver} to set.
     */
    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    /**
     * Adds a metric to the test data within the specified setup model.
     *
     * @param metric the name of the metric to add.
     * @param value  the value of the metric to add.
     */
    public void addMetrics(String metric, Object value) {
        try {
            // Retrieve the test data map from the environment object.
            Map<String, Object> testData = environment.getTestData();

            // Check if the "Metrics" key exists in the test data and if its value is a Map.
            Object metricsObject = testData.get("Metrics");
            Map<String, Object> metrics = new HashMap<>();

            // If the "Metrics" key exists and is a Map, copy its contents into the new metrics map.
            if (metricsObject instanceof Map<?, ?>) {
                // Iterate through the existing metrics and copy them into a new map.
                for (Map.Entry<?, ?> entry : ((Map<?, ?>) metricsObject).entrySet()) {
                    if (entry.getKey() instanceof String && entry.getValue() != null) {
                        metrics.put((String) entry.getKey(), entry.getValue());
                    }
                }
            }

            // Add or update the specified metric in the metrics map.
            metrics.put(metric, value);

            // Update the test data map with the modified metrics map.
            testData.put("Metrics", metrics);
        } catch (Exception e) {
            // Catch and suppress any exceptions to avoid interrupting execution.
        }
    }

    /**
     * Adds a new driver or replaces an existing driver in the given setup model.
     *
     * @param name   the name of the driver
     * @param driver the {@link WebDriver} instance to add or replace
     */
    public void addOrReplaceDriver(String name, WebDriver driver) {
        // Initialize the drivers map if it is null.
        if (drivers == null) {
            drivers = new HashMap<>();
        }

        // Add or replace the driver with the specified name.
        drivers.put(name, driver);
    }

    /**
     * Disposes of the main web driver and any additional drivers in the given setup model.
     */
    public void clearDrivers() {
        try {
            // Dispose of the main web driver if it exists.
            WebDriver mainDriver = webDriver;
            if (mainDriver != null) {
                mainDriver.quit();
            }

            if (drivers == null || drivers.isEmpty()) {
                return;
            }

            // Dispose of any additional drivers in the setup model's Drivers collection.
            for (WebDriver driver : drivers.values()) {
                if (driver != null) {
                    driver.quit();
                }
            }

            // Clear the Drivers map to remove all drivers from the setup model.
            drivers.clear();

        } catch (Exception e) {
            // Silently catch any exceptions to prevent them from affecting the overall execution.
        }
    }

    /**
     * Retrieves the properties of the currently executing test method.
     * This method inspects the stack trace to identify the test method being executed,
     * and extracts its display name, class name, and method name.
     *
     * @return a {@link TestProperties} object containing the test's display name,
     * class name, and method name. If no test method is found, returns an empty
     * {@link TestProperties} object with empty strings.
     */
    public static TestProperties getTestProperties() {
        // Get the current thread's stack trace
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        // Skip the first two elements (current method and getStackTrace call)
        int i = 2;

        // Iterate through the stack trace elements
        while (i < stackTrace.length) {
            StackTraceElement element = stackTrace[i];
            try {
                // Load the class corresponding to the current stack trace element
                Class<?> clazz = Class.forName(element.getClassName());
                Method[] methods = clazz.getDeclaredMethods(); // Get all declared methods in the class
                int j = 0;

                // Iterate through the methods of the class
                while (j < methods.length) {
                    Method method = methods[j];

                    // Check if the method name matches the stack trace element's method name
                    boolean isMethodName = method.getName().equals(element.getMethodName());

                    // Check if the method is annotated with @Test
                    boolean isTestMethod = isMethodName && method.isAnnotationPresent(Test.class);

                    // If not a test method, continue to the next method
                    if (!isTestMethod) {
                        j++;
                        continue;
                    }

                    // Retrieve the display name of the test method, if annotated with @DisplayName
                    String testDisplayName = method.isAnnotationPresent(DisplayName.class)
                            ? method.getAnnotation(DisplayName.class).value()
                            : method.getName(); // Default to the method name if no @DisplayName is present

                    // Retrieve the class name and method name
                    String className = clazz.getName();
                    String methodName = method.getName();

                    // Return the test method properties
                    return new TestProperties(testDisplayName, className, methodName);
                }
            } catch (ClassNotFoundException e) {
                // If the class can't be loaded, skip this stack trace element
            }

            // Move to the next stack trace element
            i++;
        }

        // Return an empty TestProperties object if no test method is found
        return new TestProperties("", "", "");
    }

    /**
     * Retrieves the search and load timeouts from the setup model's environment.
     *
     * @return a {@link Timeouts} containing the search timeout and load timeout.
     */
    public Timeouts getTimeouts() {
        // Retrieve the test context properties from the environment.
        // Assumes that TestContext.getProperties() returns a Map<Object, Object>
        Map<String, Object> properties = environment.getContextProperties();

        // Get the search timeout value (with a default of "10000" milliseconds) as a string.
        String searchTimeoutValue = String
                .valueOf(properties.getOrDefault("WEB_DRIVER_SEARCH_TIMEOUT", "10000"));
        int searchTimeout;
        try {
            // Try to parse the search timeout value.
            searchTimeout = Integer.parseInt(searchTimeoutValue);
        } catch (NumberFormatException e) {
            // Fallback to default if parsing fails.
            searchTimeout = 10000;
        }

        // Get the load timeout value (with a default of "30000" milliseconds) as a string.
        String loadTimeoutValue = String
                .valueOf(properties.getOrDefault("WEB_DRIVER_LOAD_TIMEOUT", "30000"));
        int loadTimeout;
        try {
            // Try to parse the load timeout value.
            loadTimeout = Integer.parseInt(loadTimeoutValue);
        } catch (NumberFormatException e) {
            // Fallback to default if parsing fails.
            loadTimeout = 30000;
        }

        // Return the parsed timeouts as Duration objects.
        return new Timeouts(Duration.ofMillis(searchTimeout), Duration.ofMillis(loadTimeout));
    }

    /**
     * Switches the current WebDriver in the setup model to a different one based on the specified key.
     *
     * @param driverKey the key of the driver to switch to.
     * @return the updated {@link TestContext} with the switched WebDriver.
     */
    public TestContext switchDriver(String driverKey) {
        // Check if the driverKey is valid (not null and not empty).
        boolean isDriverKeyValid = driverKey != null && !driverKey.isEmpty();

        // If the driverKey is invalid, return the current instance without making changes.
        if (!isDriverKeyValid) {
            return this;
        }

        // Retrieve the WebDriver associated with the given driverKey and set it as the current WebDriver.
        webDriver = drivers.get(driverKey);

        // Return the updated TestContext instance.
        return this;
    }

    /**
     * A simple tuple-like record to hold search and load timeouts.
     *
     * @param searchTimeout the search timeout duration
     * @param loadTimeout   the load timeout duration
     */
    public record Timeouts(Duration searchTimeout, Duration loadTimeout) {
    }

    /**
     * Utility class for Allure reporting.
     * Provides methods to log messages, capture screenshots, and attach them to Allure reports.
     */
    public static class AllureUtility {
        /**
         * Private constructor to prevent instantiation of the utility class.
         * This ensures that the class is used only in a static context.
         */
        private AllureUtility() {
        }

        /**
         * Adds an attachment to the Allure report.
         * This method creates a text file attachment with the specified name and content.
         *
         * @param name    the name of the attachment to be displayed in the Allure report.
         * @param content the content of the attachment as a string.
         */
        public static void newAllureAttachment(String name, String content) {
            // Convert the content string into a byte array using UTF-8 encoding.
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

            // Create a ByteArrayInputStream from the byte array for the attachment.
            ByteArrayInputStream messageStream = new ByteArrayInputStream(bytes);

            // Add the attachment to the Allure report with the specified name and content type.
            Allure.addAttachment(name, "text/plain", messageStream, ".txt");
        }

        /**
         * Logs a message as an Allure step.
         * This method is used to add a step in the Allure report with the provided message.
         *
         * @param message the message to log as an Allure step.
         */
        @Step("{message}")
        public static void newAllureMessage(String message) {
            // This method is a placeholder for an Allure step message.
            // The @Step annotation automatically logs the message in the Allure report.
        }

        /**
         * Captures a screenshot using the provided WebDriver instance and attaches it to the Allure report.
         *
         * @param title     the title for the screenshot attachment in the Allure report.
         * @param webDriver the WebDriver instance used to capture the screenshot.
         */
        public static void newAllureScreenshot(String title, WebDriver webDriver) {
            // Capture a screenshot as a Base64-encoded string.
            String base64Screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BASE64);

            // Decode the Base64 string into binary image bytes.
            byte[] imageBytes = Base64.getDecoder().decode(base64Screenshot);

            // Wrap the binary data in a ByteArrayInputStream.
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);

            // Attach the screenshot to the Allure report.
            Allure.addAttachment(title, "image/png", inputStream, ".png");
        }

        /**
         * Reads the provided screenshot file, converts it to a Base64-encoded string,
         * and attaches it to the Allure report using the specified title.
         *
         * @param title          the title for the Allure attachment.
         * @param screenshotFile the screenshot file (should be a PNG image).
         */
        public static void newAllureScreenshot(String title, File screenshotFile) {
            // Check if file name ends with .png (case-insensitive)
            if (!screenshotFile.getName().toLowerCase().endsWith(".png")) {
                return;
            }

            try {
                // Read all bytes from the file.
                byte[] fileBytes = Files.readAllBytes(screenshotFile.toPath());

                // Encode the bytes to a Base64 string.
                String base64Screenshot = Base64.getEncoder().encodeToString(fileBytes);

                // Attach the screenshot to the Allure report.
                // The attachment is named by 'title', uses MIME type "image/png", and is recognized as a PNG file.
                newAllureScreenshot(title, base64Screenshot);
            } catch (IOException e) {
                log.error(e.getCause().getMessage(), e);
            }
        }

        /**
         * Attaches a screenshot to the Allure report.
         * This method takes a Base64-encoded screenshot string, converts it into a byte array input stream,
         * and attaches it to the Allure report with the specified title.
         *
         * @param title            the title for the Allure attachment.
         * @param base64Screenshot the Base64-encoded screenshot string.
         */
        public static void newAllureScreenshot(String title, String base64Screenshot) {
            // Convert the Base64-encoded screenshot string into a byte array.
            byte[] bytes = base64Screenshot.getBytes(StandardCharsets.UTF_8);

            // Convert the Base64-encoded screenshot string into a byte array input stream.
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

            // Add the screenshot as an attachment to the Allure report with the specified title.
            Allure.addAttachment(title, "image/png", inputStream, ".png");
        }
    }

    /**
     * Represents the properties of a test method, including its name, description,
     * class name, and method name.
     *
     * @param testDisplayName A brief description of the test.
     * @param testClassName   The name of the class containing the test.
     * @param testMethodName  The name of the test method.
     */
    public record TestProperties(String testDisplayName, String testClassName, String testMethodName) {
    }
}
