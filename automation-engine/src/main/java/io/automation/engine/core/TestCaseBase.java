package io.automation.engine.core;

import io.automation.engine.exceptions.TestSetupException;
import io.automation.engine.exceptions.TestTeardownException;
import io.automation.engine.models.*;
import io.qameta.allure.Allure;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.opentest4j.TestAbortedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Abstract base class for test cases in the automation framework.
 * Provides common functionality for managing test execution, including setup, teardown,
 * retry logic, and environment initialization.
 */
@SuppressWarnings({"squid:S4275", "unused", "squid:S2142", "BusyWait"})
public abstract class TestCaseBase {
    // Logger for the TestCaseBase class, used for logging messages at the class level.
    private static final Logger logger = LoggerFactory.getLogger(TestCaseBase.class);

    // Logger specific to the instance of the subclass, used for instance-level logging.
    private final Logger instanceLogger;

    // The test result model to store results of the test case execution.
    private TestResultModel testResult;

    // The automation environment containing context properties and configurations for the test case.
    private AutomationEnvironment environment;

    // The number of attempts to retry the test case in case of failure.
    private int numberOfAttempts;

    // The interval (in milliseconds) between retry attempts for the test case.
    private int attemptsInterval;

    /**
     * Constructs a new instance of the `TestCaseBase` class.
     * Initializes the logger, automation environment, and default values for
     * the number of attempts and the interval between attempts.
     */
    protected TestCaseBase() {
        // Initialize a logger specific to the subclass of TestCaseBase.
        instanceLogger = LoggerFactory.getLogger(this.getClass());

        // Initialize the test result model to store results of the test case execution.
        testResult = new TestResultModel();

        // Initialize the automation environment by loading context properties.
        this.environment = initializeEnvironment(this.getClass());

        // Retrieve the number of attempts from the context properties, defaulting to 1 if not specified.
        String environmentNumberOfAttempts = environment
                .getContextProperties()
                .getOrDefault("NUMBER_OF_ATTEMPTS", "1")
                .toString();
        numberOfAttempts = Integer.parseInt(environmentNumberOfAttempts);

        // Retrieve the interval between attempts from the context properties, defaulting to 15000
        // milliseconds if not specified.
        String environmentAttemptsInterval = environment
                .getContextProperties()
                .getOrDefault("ATTEMPTS_INTERVAL", "15000")
                .toString();
        attemptsInterval = Integer.parseInt(environmentAttemptsInterval);
    }

    /**
     * Retrieves the automation environment associated with this test case.
     * The automation environment contains context properties and configurations
     * used during the execution of the test case.
     *
     * @return the {@link AutomationEnvironment} instance.
     */
    public AutomationEnvironment getEnvironment() {
        return environment;
    }

    /**
     * Retrieves the logger instance associated with this test case.
     * This logger is specific to the class of the test case and can be used
     * for logging messages during the test execution.
     *
     * @return the {@link Logger} instance for this test case.
     */
    public Logger getLogger() {
        // Return the logger instance specific to this test case class.
        return instanceLogger;
    }

    /**
     * Sets the automation environment for this test case.
     * The automation environment contains context properties and configurations
     * used during the execution of the test case.
     *
     * @param environment the {@link AutomationEnvironment} instance to set.
     * @return the current {@link TestCaseBase} instance for method chaining.
     */
    public TestCaseBase setEnvironment(AutomationEnvironment environment) {
        // Assign the provided environment to the instance variable.
        this.environment = environment;

        // Return the current instance to allow method chaining.
        return this;
    }

    /**
     * Sets the number of attempts for the test case.
     * This value determines how many times the test case will retry execution
     * in case of failure or other conditions requiring retries.
     *
     * @param numberOfAttempts the number of attempts to set.
     * @return the current {@link TestCaseBase} instance for method chaining.
     */
    public TestCaseBase setNumberOfAttempts(int numberOfAttempts) {
        // Assign the provided number of attempts to the instance variable.
        this.numberOfAttempts = numberOfAttempts;

        // Return the current instance to allow method chaining.
        return this;
    }

    /**
     * Retrieves the number of attempts configured for the test case.
     * This value determines how many times the test case will retry execution
     * in case of failure or other conditions requiring retries.
     *
     * @return the number of attempts.
     */
    public int getNumberOfAttempts() {
        // Return the configured number of attempts for the test case.
        return numberOfAttempts;
    }

    /**
     * Retrieves the interval between attempts for the test case.
     * This value determines the delay (in milliseconds) between retry attempts
     * during the execution of the test case.
     *
     * @return the interval between attempts in milliseconds.
     */
    public int getAttemptsInterval() {
        // Return the configured interval between attempts for the test case.
        return attemptsInterval;
    }

    /**
     * Sets the interval between retry attempts for the test case.
     * This value determines the delay (in milliseconds) between retry attempts
     * during the execution of the test case.
     *
     * @param attemptsInterval the interval between attempts in milliseconds.
     * @return the current {@link TestCaseBase} instance for method chaining.
     */
    public TestCaseBase setAttemptsInterval(int attemptsInterval) {
        // Assign the provided interval to the instance variable.
        this.attemptsInterval = attemptsInterval;

        // Return the current instance to allow method chaining.
        return this;
    }

    /**
     * Abstract method to define the main automation test logic.
     * Subclasses must implement this method to provide the specific test steps
     * to be executed during the test case.
     *
     * @param testContext the {@link TestContext} instance containing the test's context and resources.
     */
    protected abstract void automationTest(TestContext testContext);

    /**
     * Executes the test case with retry logic, capturing metrics, durations, and results.
     * This method handles multiple attempts, collects exceptions, and logs the test results.
     *
     * @return a {@link TestResultModel} containing the results of the test execution.
     */
    public TestResultModel invoke() {
        // Initialize the test context with the current environment.
        TestContext testContext = new TestContext(environment);

        // List to store exceptions encountered during test execution.
        List<TestPhaseExceptionModel> exceptions = new ArrayList<>();

        // Retrieve test properties for logging and reporting purposes.
        TestContext.TestProperties testProperties = TestContext.getTestProperties();

        // Loop through the number of attempts configured for the test case.
        for (int i = 1; i <= this.numberOfAttempts; i++) {
            // Set the current attempt number.
            int attempt = i;

            // Log the attempt number and start the test case execution.
            try {
                AllureExtensions.throwableStep("Attempt Number: " + attempt, () -> {
                    // Update the context with the current attempt number.
                    testContext.getEnvironment().getContextProperties().put("ATTEMPT", attempt);

                    // Invoke the test case execution for the current attempt.
                    testResult = invoke(attempt, this, testContext);

                    // Collect exceptions from the current attempt.
                    exceptions.addAll(testResult.getExceptions());
                    testResult.setAttemptNumber(attempt);
                    testResult.getExceptions().addAll(exceptions);

                    // Retrieve and set metrics from the environment context.
                    Object environmentMetrics = testContext
                            .getEnvironment()
                            .getContextProperties()
                            .getOrDefault("metrics", new HashMap<String, Object>());
                    Map<String, Object> metrics = Utilities.convertToMap(environmentMetrics);
                    testResult.setMetrics(metrics);

                    // Retrieve and set auditable actions from the environment context.
                    Object environmentAuditableActions = testContext
                            .getEnvironment()
                            .getContextProperties()
                            .getOrDefault("auditableActions", new HashMap<String, Object>());
                    Map<String, Object> auditableActions = Utilities.convertToMap(environmentAuditableActions);
                    testResult.setAuditableActions(auditableActions);

                    // Retrieve and set the duration for finding elements during the test.
                    long findElementsDuration = (long) testContext
                            .getEnvironment()
                            .getTestData()
                            .getOrDefault("findElementsDuration", 0L);
                    testResult.setFindElementsDuration(Duration.ofNanos(findElementsDuration));

                    // Calculate and set the durations for setup, teardown, and test execution phases.
                    testResult.setSetupDuration(Duration.between(testResult.getSetupStartTime(), testResult.getSetupEndTime()));
                    testResult.setTeardownDuration(Duration.between(testResult.getTeardownStartTime(), testResult.getTeardownEndTime()));
                    testResult.setTestDuration(Duration.between(testResult.getTestStartTime(), testResult.getTestEndTime()));

                    // Set display name, class name, and method name for the test result.
                    testResult.setDisplayName(testProperties.testDisplayName());
                    testResult.setTestClassName(testProperties.testClassName());
                    testResult.setTestMethodName(testProperties.testMethodName());

                    // Determine the result of the test case (Pass/Fail) and log it.
                    String result = testResult.isPassed() ? "Pass" : "Fail";
                    String message = "Test case '{}' has completed with a result of '{}'.";
                    logger.info(message, this.getClass().getName(), result);

                    // throw the last exception if the test case failed
                    confirmTestCase("Test case failed on retry attempt: " + attempt, testResult);
                });
            } catch (Throwable e) {
                // Silently catch any exceptions that occur during the test case execution.
            }

            // If the test passed, exit the retry loop.
            if (testResult.isPassed()) {
                break;
            }

            // If the test failed and more attempts are allowed, wait before retrying.
            if (attempt < numberOfAttempts) {
                try {
                    Thread.sleep(attemptsInterval);
                } catch (Exception e) {
                    // Log any errors that occur during the wait period.
                    logger.error("Error during test case execution: ", e);
                }
            }
        }

        // throw the last exception if the test case failed
        confirmTestCase("Test case failed after all retry attempts.", testResult);

        // Return the final test result after all attempts.
        return testResult;
    }

    // Invokes the test case execution, including setup, test execution, and teardown phases.
    // Handles retries, exception logging, and AllureExtensions reporting.
    private static TestResultModel invoke(int attempt, TestCaseBase testCase, TestContext testContext) {
        // Generate a default run ID if none is provided in the context properties.
        String defaultRunId = AssemblyInitialize.TEST_RUN_ID;
        String runId = testContext
                .getEnvironment()
                .getContextProperties()
                .getOrDefault("TEST_RUN_ID", defaultRunId).toString();

        // Initialize the test result model and set the run ID.
        TestResultModel testResult = new TestResultModel();
        testResult.setRunId(runId);

        // List to store exceptions encountered during the test execution.
        List<TestPhaseExceptionModel> exceptions = new ArrayList<>();

        try {
            // Execute the setup phase and update the test result with setup results.
            TestPhaseResultModel setupResult =
                    Allure.step("Test Setup Phase", () -> setup(testCase, testContext));
            testResult.updateSetupResults(setupResult);

            // If a setup exception occurred, add it to the exceptions list and abort the test.
            if (setupResult.getPhaseException() != null) {
                exceptions.add(setupResult.getPhaseException());
                throw new TestAbortedException(setupResult.getPhaseException().getReasonPhrase());
            }

            // Record the start time of the test execution phase.
            testResult.setTestStartTime(LocalDateTime.now());

            // Execute the test case's main logic within the "Test Invocation Phase".
            AllureExtensions.throwableStep("Test Invocation Phase", () -> testCase.automationTest(testContext));

            // Mark the test as passed if no exceptions occurred.
            testResult.setPassed(true);

            // Return the test result after successful execution.
            return testResult;

        } catch (TestAbortedException e) {
            // Attach the exception details to the AllureExtensions report and rethrow the exception.
            newExceptionAttachment(e);
            throw e;
        } catch (Throwable e) {
            // Handle unexpected exceptions during test execution.
            TestPhaseExceptionModel exception = newException(e, attempt, testContext);

            // Add the exception to the list of exceptions encountered during the test.
            testResult.addException(exception);

            // Attach the exception details and a screenshot to the AllureExtensions report.
            newExceptionAttachment(e);

            // Add screenshot to the AllureExtensions report if available.
            if (exception.getScreenshot() != null && !exception.getScreenshot().isEmpty()) {
                String title = testResult.getRunId() + "." + testCase.getClass().getName() + "." + attempt;
                TestContext.AllureUtility.newAllureScreenshot(title, exception.getScreenshot());
            }

            // Return the test result even if an exception occurred.
            return testResult;
        } finally {
            // Record the end time of the test execution phase.
            testResult.setTestEndTime(LocalDateTime.now());

            // Add any collected exceptions to the test result.
            testResult.getExceptions().addAll(exceptions);

            // Execute the teardown phase and update the test result with teardown results.
            TestPhaseResultModel teardownResult =
                    Allure.step("Test Teardown Phase", () -> teardown(testCase, testContext));
            testResult.updateTeardownResults(teardownResult);

            // Clear all WebDriver instances from the test context.
            testContext.clearDrivers();
        }
    }

    /**
     * Confirms the test case result by checking if it passed without any exceptions.
     * If the test case failed, it throws an assertion failure with the provided message
     * and the last encountered exception.
     *
     * @param message    the message to include in the assertion failure if the test case failed.
     * @param testResult the {@link TestResultModel} containing the test case results.
     */
    private static void confirmTestCase(String message, TestResultModel testResult) {
        // Check if the test case passed without any exceptions.
        if (testResult.getExceptions().isEmpty() && testResult.isPassed()) {
            return;
        }

        // Retrieve the last exception if the test case failed.
        Throwable exception = testResult.getExceptions().isEmpty()
                ? null
                : testResult.getExceptions().get(testResult.getExceptions().size() - 1).getException();

        // If the exception is null, use the provided message.
        message = exception == null ? message : Utilities.getBaseException(exception).getMessage();

        // Fail the test case with the provided message and the last encountered exception.
        Assertions.fail(message, exception);
    }

    /**
     * Performs additional setup actions before the test execution.
     * This method can be overridden by subclasses to implement custom setup logic,
     * such as initializing resources or configuring test data.
     *
     * @param testContext the {@link TestContext} instance containing the test's context and resources.
     */
    protected void onSetup(TestContext testContext) {
        // This method can be overridden by subclasses to perform additional setup actions.
    }

    /**
     * Performs additional teardown actions after the test execution.
     * This method can be overridden by subclasses to implement custom teardown logic,
     * such as releasing resources or cleaning up test data.
     *
     * @param testContext the {@link TestContext} instance containing the test's context and resources.
     */
    protected void onTeardown(TestContext testContext) {
        // This method can be overridden by subclasses to perform additional teardown actions.
    }

    // Executes the setup phase for the test case.
    // This method initializes the test environment, performs setup actions, and captures the results.
    private static TestPhaseResultModel setup(TestCaseBase testCase, TestContext testContext) {
        // Create a new result model to store the setup phase results.
        TestPhaseResultModel result = new TestPhaseResultModel();

        // Set the phase name to "Setup" and record the start time.
        result.setPhaseName("Setup");
        result.setStartTime(LocalDateTime.now());

        try {
            // Initialize the WebDriver or other required resources for the test.
            initializeDriver(testContext);

            // Allow the test case to perform additional setup actions.
            testCase.onSetup(testContext);

            // Mark the setup phase as successful.
            result.setSuccess(true);
        } catch (Exception e) {
            // Wrap the exception in a TestSetupException with a descriptive message.
            TestSetupException exception = new TestSetupException("An error occurred during test setup.", e);

            // Mark the setup phase as unsuccessful and record the exception details.
            result.setSuccess(false);
            result.setPhaseException(new TestPhaseExceptionModel(exception));
        } finally {
            // Record the end time and calculate the duration of the setup phase.
            result.setEndTime(LocalDateTime.now());
            result.setDuration(Duration.between(result.getStartTime(), result.getEndTime()));
        }

        // Return the result of the setup phase.
        return result;
    }

    // Executes the teardown phase for the test case.
    // This method performs cleanup actions after the test execution, such as releasing resources,
    // and captures the results of the teardown phase.
    private static TestPhaseResultModel teardown(TestCaseBase testCase, TestContext testContext) {
        // Create a new result model to store the teardown phase results.
        TestPhaseResultModel result = new TestPhaseResultModel();

        // Set the phase name to "Teardown" and record the start time.
        result.setPhaseName("Teardown");
        result.setStartTime(LocalDateTime.now());

        try {
            // Allow the test case to perform additional teardown actions.
            testCase.onTeardown(testContext);

            // Mark the teardown phase as successful.
            result.setSuccess(true);
        } catch (Exception e) {
            // Wrap the exception in a TestTeardownException with a descriptive message.
            TestTeardownException exception = new TestTeardownException("An error occurred during test teardown.", e);

            // Mark the teardown phase as unsuccessful and record the exception details.
            result.setSuccess(false);
            result.setPhaseException(new TestPhaseExceptionModel(exception));
        } finally {
            // Record the end time and calculate the duration of the teardown phase.
            result.setEndTime(LocalDateTime.now());
            result.setDuration(Duration.between(result.getStartTime(), result.getEndTime()));
        }

        // Return the result of the teardown phase.
        return result;
    }

    // Attaches the stack trace of a given exception to the AllureExtensions report as a text file.
    private static void newExceptionAttachment(Throwable e) {
        // Create a StringWriter to capture the exception's stack trace as a string.
        StringWriter stringWriter = new StringWriter();

        // Write the exception's stack trace to the StringWriter using a PrintWriter.
        e.printStackTrace(new PrintWriter(stringWriter));

        // Convert the captured stack trace to a string.
        String exceptionStackTrace = stringWriter.toString();

        // Create new AllureExtensions attachment with the stack trace.
        TestContext.AllureUtility.newAllureAttachment("Exception Stack Trace", exceptionStackTrace);
    }

    // Initializes the WebDriver instance for the test context.
    // This method determines the type of WebDriver to use (local or remote)
    // based on the context properties and sets it in the test context.
    private static void initializeDriver(TestContext testContext) {
        // Retrieve the driver type from the context properties, defaulting to "chrome" if not specified.
        String driverType = testContext
                .getEnvironment()
                .getContextProperties()
                .getOrDefault("DRIVER_TYPE", "")
                .toString();

        // If the driver type is not specified, return early.
        if (driverType.isEmpty()) {
            logger.info("Driver type is not specified. Skipping driver initialization.");
            return;
        }

        // Retrieve the remote WebDriver address from the context properties, defaulting to an empty string if not specified.
        String driverRemoteAddress = testContext
                .getEnvironment()
                .getContextProperties()
                .getOrDefault("DRIVER_REMOTE_ADDRESS", "")
                .toString();

        // Initialize the WebDriver based on whether a remote address is provided.
        // If no remote address is specified, start a local WebDriver instance.
        WebDriver driver = driverRemoteAddress.isEmpty()
                ? DriverFactory.start(driverType) // Start a local WebDriver.
                : DriverFactory.start(driverType, driverRemoteAddress); // Start a remote WebDriver.

        // Set the initialized WebDriver in the test context.
        testContext.setWebDriver(driver);

        // Add the WebDriver to the context's driver map with the key "default".
        testContext.getDrivers().put("default", driver);
    }

    // Initializes the automation environment for the given test case.
    // This method loads environment properties from resource files and populates
    // an instance of `AutomationEnvironment` with the loaded properties.
    public static AutomationEnvironment initializeEnvironment(Class<?> testCaseClass) {
        try {
            // Load global environment properties from the .env file.
            Utilities.publishEnvironmentProperties(testCaseClass, ".env");

            // Retrieve the AUT (Application Under Test) identifier from system properties.
            String autName = System.getProperty("AUT");

            // Load environment properties specific to the AUT.
            Utilities.publishEnvironmentProperties(testCaseClass, "aut/" + autName + ".env");

            // Retrieve all environment properties as a map.
            Map<String, Object> environmentProperties = Utilities.getEnvironmentProperties();

            // Create a new AutomationEnvironment instance.
            AutomationEnvironment environment = new AutomationEnvironment();

            // Populate the environment with the retrieved properties.
            environment.getContextProperties().putAll(environmentProperties);

            // Return the initialized environment.
            return environment;
        } catch (Exception e) {
            // Log the error and return a default AutomationEnvironment instance.
            logger.error("Failed to initialize environment", e);
            return new AutomationEnvironment();
        }
    }

    // Creates a new instance of {@link TestPhaseExceptionModel} with details about the exception,
    // including the attempt number, context properties, display name, and a screenshot if available.
    private static TestPhaseExceptionModel newException(Throwable exception, int attempt, TestContext testContext) {
        // Create a new TestPhaseExceptionModel instance with the provided exception.
        TestPhaseExceptionModel exceptionModel = new TestPhaseExceptionModel(exception);

        // Set the attempt reference, context properties, and display name for the exception model.
        exceptionModel
                .setAttemptReference(attempt)
                .setContext(testContext.getEnvironment().getContextProperties())
                .setDisplayName(TestContext.getTestProperties().testDisplayName());

        // If the WebDriver is null, return the exception model without adding a screenshot.
        if (testContext.getWebDriver() == null) {
            return exceptionModel;
        }

        // Capture a screenshot from the WebDriver and encode it as a Base64 string.
        String screenshot = ((TakesScreenshot) testContext.getWebDriver()).getScreenshotAs(OutputType.BASE64);

        // Add the screenshot to the exception model.
        exceptionModel.setScreenshot(screenshot);

        // Return the populated exception model.
        return exceptionModel;
    }
}
