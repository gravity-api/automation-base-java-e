package io.automation.engine.core;

import io.automation.engine.exceptions.AutomationException;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class PageModelBase implements FluentModel {
    // Logger for the TestCaseBase class, used for logging messages at the class level.
    private static final Logger logger = LoggerFactory.getLogger(PageModelBase.class);

    // Holds the test context containing configuration and WebDriver information
    private final TestContext testContext;

    // Holds the WebDriverWait instance for waiting on specific conditions or elements
    private final WebDriverWait webDriverWait;

    /**
     * Constructs a `PageModelBase` instance with the given `TestContext`.
     * This constructor delegates to the other constructor, passing `null` as the URI.
     *
     * @param testContext the `TestContext` containing configuration and WebDriver information
     */
    protected PageModelBase(TestContext testContext) {
        // Delegate to the other constructor with a null URI
        this(testContext, null);
    }

    /**
     * Constructs a `PageModelBase` instance with the given `TestContext` and optional URI.
     * If a non-null URI is provided, the WebDriver navigates to the specified URI.
     *
     * @param testContext the `TestContext` containing configuration and WebDriver information
     * @param uri         the URI to navigate to, or `null` if no navigation is required
     */
    protected PageModelBase(TestContext testContext, String uri) {
        // Initialize the test context
        this.testContext = testContext;

        // Initialize the WebDriverWait with the WebDriver from the test context
        this.webDriverWait =
                new WebDriverWait(testContext.getWebDriver(), Duration.from(testContext.getTimeouts().searchTimeout()));

        // Navigate to the specified URI if it is not null
        if (uri != null) {
            testContext.getWebDriver().get(uri);
            testContext.getWebDriver().manage().window().maximize();
        }
    }

    /**
     * Retrieves the `TestContext` associated with this model.
     * The `TestContext` contains configuration and WebDriver information.
     *
     * @return the `TestContext` instance
     */
    public TestContext getTestContext() {
        // Return the test context instance
        return testContext;
    }

    /**
     * Retrieves the `WebDriver` instance associated with the current `TestContext`.
     * The `WebDriver` is used to interact with the browser during test execution.
     *
     * @return the `WebDriver` instance
     */
    public WebDriver getWebDriver() {
        // Access and return the WebDriver from the TestContext
        return testContext.getWebDriver();
    }

    /**
     * Retrieves the `WebDriverWait` instance associated with this model.
     * The `WebDriverWait` is used to wait for specific conditions or elements during test execution.
     *
     * @return the `WebDriverWait` instance
     */
    public WebDriverWait getWebDriverWait() {
        // Return the WebDriverWait instance initialized with the TestContext's WebDriver and timeouts
        return webDriverWait;
    }

    /**
     * Executes an auditable action, logs the action performed, and records the duration.
     *
     * @param action          the name of the action to be performed.
     * @param actionToPerform a function that takes an ObjectSetupModel and returns a result of type T.
     * @param <T>             the type of the result.
     * @return the result of the action performed.
     */
    @SuppressWarnings({"unchecked", "squid:S2139"})
    public <T> T auditableAction(String action, Function<TestContext, T> actionToPerform) {
        try {
            // Retrieve test data map from the environment.
            Map<String, Object> testData = testContext.getEnvironment().getTestData();
            final String AUDITABLE_ACTIONS = "auditableActions";

            // Check if the "AuditableActions" key exists and holds a Map.
            Map<String, Object> auditableActions =
                    (Map<String, Object>) testData.getOrDefault(AUDITABLE_ACTIONS, new HashMap<>());

            // Start a timer (using nanoseconds).
            long startTime = System.nanoTime();
            try {
                // Log the success message.
                return Allure.step("Performing Auditable Action: " + action, () -> {
                    // Perform the specified action.
                    T result = actionToPerform.apply(testContext);

                    // Update the test data with the modified auditable actions map.
                    testData.put("AuditableActions", auditableActions);

                    // Return the result of the action.
                    return result;
                });

            } finally {
                // Stop the timer and calculate duration in ticks (1 tick = 100 nanoseconds).
                long elapsedNanos = System.nanoTime() - startTime;
                long ticks = elapsedNanos / 100;
                auditableActions.put(action + "Duration", ticks);
            }
        } catch (Exception e) {
            // Log the full exception instead of just its cause.
            logger.error("Error performing auditable action: {}", action, e);

            // Rethrow a new exception with the full context.
            throw new AutomationException("Error occurred while performing auditable action: " + action, e);
        }
    }

    /**
     * Switches to a new instance of the specified model type.
     *
     * @param type the {@code Class} of the model to switch to
     * @return a new instance of the specified model type
     */
    @Override
    public <T extends PageModelBase> T switchModel(Class<T> type) {
        return ModelFactory.newModel(type);
    }

    /**
     * Switches to a new instance of the specified model type with the given setup model.
     *
     * @param type        the {@code Class} of the model to switch to
     * @param testContext the setup model containing configuration and WebDriver information
     * @return a new instance of the specified model type
     */
    @Override
    public <T extends PageModelBase> T switchModel(Class<T> type, TestContext testContext) {
        return ModelFactory.newModel(type, testContext);
    }

    /**
     * Switches to a new instance of the specified model type with additional arguments.
     *
     * @param type        the {@code Class} of the model to switch to
     * @param testContext the setup model containing configuration and WebDriver information
     * @param arguments   additional arguments to pass to the model's constructor
     * @return a new instance of the specified model type
     */
    @Override
    public <T extends PageModelBase> T switchModel(Class<T> type, TestContext testContext, Object... arguments) {
        // Use Optional.ofNullable to ensure that if arguments is null,
        // we default to an empty array.
        Object[] combinedArguments = Stream.concat(
                Stream.of(testContext),
                Arrays.stream(Optional.ofNullable(arguments).orElse(new Object[0]))
        ).toArray();

        // Create a new model instance using the ModelFactory.
        return ModelFactory.newModel(type, combinedArguments);
    }
}
