package io.automation.engine.core;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * A factory class for creating and managing WebDriver instances.
 * This class provides various static methods to initialize WebDriver instances
 * for local or remote execution with different configurations and capabilities.
 */
public class DriverFactory {
    /**
     * Private constructor to prevent instantiation of the DriverFactory class.
     * This class is designed to provide static methods only.
     */
    private DriverFactory() {
    }

    /**
     * Starts a WebDriver instance with default Chrome options.
     * This method initializes a ChromeOptions object with default arguments
     * and starts a WebDriver instance for Chrome using these options.
     *
     * @return A WebDriver instance configured for Chrome.
     */
    public static WebDriver start() {
        // Create a new ChromeOptions instance to configure the Chrome browser.
        ChromeOptions chromeOptions = new ChromeOptions();

        // Add default Chromium-based browser arguments to the ChromeOptions instance.
        chromeOptions.addArguments(getChromiumArguments());

        // Start a WebDriver instance for Chrome with the specified options.
        return start(Drivers.CHROME, "", chromeOptions);
    }

    /**
     * Starts a WebDriver instance using the specified driver class.
     *
     * @param driverClass The class of the WebDriver to instantiate.
     * @param <T>         The type of the WebDriver.
     * @return A new instance of the specified WebDriver class.
     */
    public static <T extends WebDriver> T start(Class<T> driverClass) {
        // Delegate to the `newDriver` method to create the WebDriver instance with no service and no options.
        return newDriver(driverClass, null, null);
    }

    /**
     * Starts a WebDriver instance using the specified driver class and capabilities.
     *
     * @param driverClass The class of the WebDriver to instantiate.
     * @param options     The capabilities to configure the WebDriver instance (can be null).
     * @param <T>         The type of the WebDriver.
     * @return A new instance of the specified WebDriver class.
     */
    public static <T extends WebDriver> T start(Class<T> driverClass, MutableCapabilities options) {
        // Delegate to the `newDriver` method to create the WebDriver instance with the provided class and options.
        return newDriver(driverClass, null, options);
    }

    /**
     * Starts a WebDriver instance using the specified driver class and service.
     *
     * @param driverClass The class of the WebDriver to instantiate.
     * @param service     The DriverService to use for the WebDriver instance (can be null).
     * @param <T>         The type of the WebDriver.
     * @return A new instance of the specified WebDriver class.
     */
    public static <T extends WebDriver> T start(Class<T> driverClass, DriverService service) {
        // Delegate to the `newDriver` method to create the WebDriver instance with the provided service and no options.
        return newDriver(driverClass, service, null);
    }

    /**
     * Starts a WebDriver instance using the specified driver class, service, and capabilities.
     *
     * @param driverClass The class of the WebDriver to instantiate.
     * @param service     The DriverService to use for the WebDriver instance (can be null).
     * @param options     The capabilities to configure the WebDriver instance (can be null).
     * @param <T>         The type of the WebDriver.
     * @return A new instance of the specified WebDriver class.
     */
    public static <T extends WebDriver> T start(
            Class<T> driverClass, DriverService service, MutableCapabilities options) {
        // Delegate to the `newDriver` method to create the WebDriver instance.
        return newDriver(driverClass, service, options);
    }

    /**
     * Starts a WebDriver instance for remote execution with default Chrome options.
     *
     * @param remoteAddress The URL of the remote WebDriver server.
     * @return A WebDriver instance connected to the remote server.
     */
    public static WebDriver start(URL remoteAddress) {
        // Create a new RemoteWebDriver instance using the provided remote address and default Chrome options.
        return newDriver(remoteAddress, new ChromeOptions());
    }

    /**
     * Starts a WebDriver instance for remote execution.
     *
     * @param remoteAddress The URL of the remote WebDriver server.
     * @param options       The capabilities to configure the WebDriver instance.
     * @return A WebDriver instance connected to the remote server.
     */
    public static WebDriver start(URL remoteAddress, MutableCapabilities options) {
        // Delegate to the `newDriver` method to create a RemoteWebDriver instance.
        return newDriver(remoteAddress, options);
    }

    /**
     * Starts a WebDriver instance based on the provided driver type.
     * This method delegates to the `newLocal` method with null capabilities.
     *
     * @param driverType The type of the driver (e.g., "CHROME", "FIREFOX").
     * @return A WebDriver instance for local execution.
     */
    public static WebDriver start(String driverType) {
        // Delegate to the `newLocal` method with the specified driver type and null capabilities.
        return newLocal(driverType, null);
    }

    /**
     * Starts a WebDriver instance using the specified driver path and capabilities.
     *
     * @param driverPath The path or URL to the driver executable or remote server.
     * @param options    The capabilities to configure the WebDriver instance.
     * @return A WebDriver instance, either local or remote, based on the driver path.
     */
    public static WebDriver start(String driverPath, MutableCapabilities options) {
        // Delegate to the overloaded `start` method, using the browser name from the options.
        return start(options.getBrowserName(), driverPath, options);
    }

    /**
     * Starts a WebDriver instance based on the provided driver type and driver path.
     * This method delegates to the overloaded `start` method with capabilities set to null.
     *
     * @param driverType The type of the driver (e.g., "CHROME", "FIREFOX").
     * @param driverPath The path or URL to the driver executable or remote server.
     * @return A WebDriver instance, either local or remote, based on the driver path.
     */
    public static WebDriver start(String driverType, String driverPath) {
        // Delegate to the overloaded method with null capabilities.
        return start(driverType, driverPath, null);
    }

    /**
     * Starts a WebDriver instance based on the provided driver type, driver path, and capabilities.
     *
     * @param driverType  The type of the driver (e.g., "CHROME", "FIREFOX").
     * @param driverPath  The path or URL to the driver executable or remote server.
     * @param options     The capabilities to configure the WebDriver instance.
     * @return A WebDriver instance, either local or remote, based on the driver path.
     */
    public static WebDriver start(String driverType, String driverPath, MutableCapabilities options) {
        // Check if the driverPath is a URL (indicating a remote WebDriver).
        boolean isRemote = driverPath.matches("^https?://.*");

        // Return a remote or local WebDriver instance based on the isRemote flag.
        return isRemote ?
                newRemote(driverType, driverPath, options) :
                newLocal(driverType, options);
    }

    // Creates a new instance of a WebDriver for remote execution
    @SuppressWarnings({"squid:S112"})
    private static WebDriver newRemote(String driverType, String uri, MutableCapabilities options) {
        // Determine the driver type based on the provided options if not explicitly specified.
        driverType = driverType.isEmpty()
                ? options.getBrowserName()
                : driverType;

        // Default Chrome options with the predefined arguments.
        ChromeOptions defaultChromeOptions = new ChromeOptions();
        defaultChromeOptions.addArguments(getChromiumArguments());

        // Default Edge options with the predefined arguments.
        EdgeOptions defaultEdgeOptions = new EdgeOptions();
        defaultEdgeOptions.addArguments(getChromiumArguments());

        try {
            // Convert the provided URL string to a URL object.
            URL remoteAddress = URI.create(uri).toURL();

            // Create and return a RemoteWebDriver instance based on the driver type.
            return switch (driverType.toUpperCase()) {
                case Drivers.FIREFOX -> new RemoteWebDriver(remoteAddress, options == null ? new FirefoxOptions() : options);
                case Drivers.EDGE -> new RemoteWebDriver(remoteAddress, options == null ? defaultEdgeOptions : options);
                case Drivers.SAFARI -> new RemoteWebDriver(remoteAddress, options == null ? new SafariOptions() : options);
                default -> new RemoteWebDriver(remoteAddress, options == null ? defaultChromeOptions : options);
            };
        } catch (Exception e) {
            // Throw a runtime exception if the WebDriver instance cannot be created.
            throw new RuntimeException("Failed to create driver", e);
        }
    }

    // Creates a new instance of a WebDriver for local execution
    @SuppressWarnings({"squid:S112"})
    private static WebDriver newLocal(String driverType, MutableCapabilities options) {
        // Determine the driver type based on the provided options if not explicitly specified.
        driverType = driverType.isEmpty()
                ? options.getBrowserName()
                : driverType;

        // Default Chrome options with the predefined arguments.
        ChromeOptions defaultChromeOptions = new ChromeOptions();
        defaultChromeOptions.addArguments(getChromiumArguments());

        // Default Edge options with the predefined arguments.
        EdgeOptions defaultEdgeOptions = new EdgeOptions();
        defaultEdgeOptions.addArguments(getChromiumArguments());

        try {
            // Create and return a RemoteWebDriver instance based on the driver type.
            return switch (driverType.toUpperCase()) {
                case Drivers.FIREFOX -> new FirefoxDriver(options == null ? new FirefoxOptions() : new FirefoxOptions().merge(options));
                case Drivers.EDGE -> new EdgeDriver(options == null ? defaultEdgeOptions : new EdgeOptions().merge(options));
                case Drivers.SAFARI -> new SafariDriver(options == null ? new SafariOptions() : new SafariOptions().merge(options));
                default -> new ChromeDriver(options == null ? defaultChromeOptions : new ChromeOptions().merge(options));
            };
        } catch (Exception e) {
            // Throw a runtime exception if the WebDriver instance cannot be created.
            throw new RuntimeException("Failed to create driver", e);
        }
    }

    // Retrieves the default arguments for Chromium-based browsers.
    private  static  List<String> getChromiumArguments() {
        // Default arguments for Chromium-based browsers.
        List<String> defaultChromiumArguments = new ArrayList<>();

        try {
            // Create a temporary unique directory for Edge user data
            Path tempProfile = Files.createTempDirectory("chromium-profile");
            defaultChromiumArguments.add("--user-data-dir=" + tempProfile.toAbsolutePath());

        } catch (Exception e) {
            // Do nothing if the temporary directory cannot be created.
        }

        // Add default arguments to the list.
        defaultChromiumArguments.add("--disable-extensions");     // Disable browser extensions.
        defaultChromiumArguments.add("--disable-gpu");            // Disable GPU acceleration.
        defaultChromiumArguments.add("--disable-dev-shm-usage");  // Avoid shared memory issues.
        defaultChromiumArguments.add("--no-sandbox");             // Disable the sandbox for compatibility.

        // Return the list of default arguments.
        return defaultChromiumArguments;
    }

    // Creates a new instance of a WebDriver using the specified driver class, service, and options.
    @SuppressWarnings({"squid:S112"})
    private static <T extends WebDriver> T newDriver(
            Class<T> driverClass,
            DriverService service,
            MutableCapabilities options) {

        // Prepare a list to hold the constructor arguments.
        List<Object> args = new ArrayList<>();

        // Add the service to the argument list if provided.
        if (service != null) {
            args.add(service);
        }

        // Add the options to the argument list if provided.
        if (options != null) {
            args.add(options);
        }

        // Convert the argument list to an array of classes for constructor matching.
        Class<?>[] classes = args.stream().map(Object::getClass).toArray(Class<?>[]::new);

        try {
            // Retrieve the constructor of the driver class that matches the argument types.
            Constructor<T> constructor = driverClass.getDeclaredConstructor(classes);

            // Instantiate the WebDriver using the retrieved constructor and arguments.
            return constructor.newInstance(args.toArray());
        } catch (Exception e) {

            // Throw a RuntimeException if instantiation fails, including the original exception.
            throw new RuntimeException("Failed to instantiate driver of type " + driverClass.getName(), e);
        }
    }

    // Creates a new instance of a RemoteWebDriver using the specified remote address and capabilities.
    private static RemoteWebDriver newDriver(URL remoteAddress, MutableCapabilities options) {
        // Instantiate and return a RemoteWebDriver with the provided remote address and capabilities.
        return new RemoteWebDriver(remoteAddress, options);
    }

    public static class Drivers {
        private Drivers(){
        }

        public static final String CHROME = "CHROME";
        public static final String FIREFOX = "FIREFOX";
        public static final String EDGE = "EDGE";
        public static final String SAFARI = "SAFARI";
    }
}
