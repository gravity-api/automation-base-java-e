package io.automation.engine.core;

import io.automation.engine.exceptions.TestSetupException;
import io.automation.engine.exceptions.TestTeardownException;
import io.automation.engine.models.*;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.opentest4j.TestAbortedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class TestCaseBase {
    private static final Logger log = LoggerFactory.getLogger(TestCaseBase.class);
    private AutomationEnvironment environment;
    private int numberOfAttempts;
    private int attemptsInterval;

    protected TestCaseBase() {
        environment = initializeEnvironment(this);

        numberOfAttempts = (int) environment
                .getContextProperties()
                .getOrDefault("NUMBER_OF_ATTEMPTS", 1);

        attemptsInterval = (int) environment
                .getContextProperties()
                .getOrDefault("ATTEMPTS_INTERVAL", 15000);
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

    protected abstract void automationTest(TestContext setupModel);

    public TestResultModel invoke() {
        TestContext setupModel = new TestContext();
        setupModel.setEnvironment(environment);

        return invoke(0, this, setupModel);
    }

    private static TestResultModel invoke(int attempt, TestCaseBase testCase, TestContext setupModel) {

        String defaultRunId = java.util.UUID.randomUUID().toString();
        String runId = setupModel
                .getEnvironment()
                .getContextProperties()
                .getOrDefault("RUN_ID", defaultRunId).toString();

        TestResultModel testResult = new TestResultModel();
        testResult.setRunId(runId);

        List<TestPhaseExceptionModel> exceptions = new ArrayList<>();

        try {
            TestPhaseResultModel setupResult = Allure.step("Test Setup Phase", () -> setup(testCase, setupModel));
            testResult.updateSetupResults(setupResult);

            if (setupResult.getPhaseException() != null) {
                exceptions.add(setupResult.getPhaseException());
                throw new TestAbortedException(setupResult.getPhaseException().getReasonPhrase());
            }

            testResult.setTestStartTime(LocalDateTime.now());

            Allure.step("Test Invocation Phase", () -> testCase.automationTest(setupModel));

            testResult.setPassed(true);

            return testResult;

        } catch (TestAbortedException e) {
            newExceptionAttachment(e);
            throw e;
        } catch (Exception e) {
            TestPhaseExceptionModel exception = newException(e, attempt, setupModel);
            String title = testResult.getRunId() + testCase.getClass().getName() + attempt;

            newExceptionAttachment(e);
            TestContext.AllureUtility.newAllureScreenshot(title, exception.getScreenshot());

            return testResult;
        } finally {
            testResult.setTestEndTime(LocalDateTime.now());
            testResult.setExceptions(exceptions);

            // teardown results
            TestPhaseResultModel teardownResult = Allure.step("Test Teardown Phase", () -> teardown(testCase, setupModel));

            testResult.updateTeardownResults(teardownResult);
        }
    }

    protected void onSetup(TestContext setupModel) {
        // This method can be overridden by subclasses to perform additional setup actions.
    }

    protected void onTeardown(TestContext setupModel) {
        // This method can be overridden by subclasses to perform additional teardown actions.
    }

    // Executes the setup phase for the test case.
    // This method initializes the test environment, performs setup actions, and captures the results.
    private static TestPhaseResultModel setup(TestCaseBase testCase, TestContext setupModel) {
        // Create a new result model to store the setup phase results.
        TestPhaseResultModel result = new TestPhaseResultModel();

        // Set the phase name to "Setup" and record the start time.
        result.setPhaseName("Setup");
        result.setStartTime(LocalDateTime.now());

        try {
            // Initialize the WebDriver or other required resources for the test.
            initializeDriver(setupModel);

            // Allow the test case to perform additional setup actions.
            testCase.onSetup(setupModel);

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

    private static TestPhaseResultModel teardown(TestCaseBase testCase, TestContext setupModel) {
        TestPhaseResultModel result = new TestPhaseResultModel();

        result.setPhaseName("Teardown");
        result.setStartTime(LocalDateTime.now());

        try {
            testCase.onTeardown(setupModel);
            result.setSuccess(true);
        } catch (Exception e) {
            TestTeardownException exception = new TestTeardownException("An error occurred during test teardown.", e);

            result.setSuccess(false);
            result.setPhaseException(new TestPhaseExceptionModel(exception));
        } finally {
            result.setEndTime(LocalDateTime.now());
            result.setDuration(Duration.between(result.getStartTime(), result.getEndTime()));
        }

        return result;
    }

    // Attaches the stack trace of a given exception to the Allure report as a text file.
    private static void newExceptionAttachment(Throwable e) {
        // Create a StringWriter to capture the exception's stack trace as a string.
        StringWriter stringWriter = new StringWriter();

        // Write the exception's stack trace to the StringWriter using a PrintWriter.
        e.printStackTrace(new PrintWriter(stringWriter));

        // Convert the captured stack trace to a string.
        String exceptionStackTrace = stringWriter.toString();

        // Create new Allure attachment with the stack trace.
        TestContext.AllureUtility.newAllureAttachment("Exception Stack Trace", exceptionStackTrace);
    }

    private static void initializeDriver(TestContext setupModel) {
        setupModel.setWebDriver(new ChromeDriver());
        // This method can be overridden by subclasses to perform additional driver initialization actions.
    }

    // Initializes the automation environment for the given test case.
    // This method loads environment properties from resource files and populates
    // an instance of `AutomationEnvironment` with the loaded properties.
    private static AutomationEnvironment initializeEnvironment(TestCaseBase testCase) {
        try {
            // Load global environment properties from the .env file.
            Utilities.publishEnvironmentProperties(testCase.getClass(), ".env");

            // Retrieve the AUT (Application Under Test) identifier from system properties.
            String autName = System.getProperty("AUT");

            // Load environment properties specific to the AUT.
            Utilities.publishEnvironmentProperties(testCase.getClass(), "aut/" + autName + ".env");

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
            log.error("Failed to initialize environment", e);
            return new AutomationEnvironment();
        }
    }

    // Creates a new instance of {@link TestPhaseExceptionModel} with details about the exception,
    // including the attempt number, context properties, display name, and a screenshot if available.
    private static TestPhaseExceptionModel newException(Exception exception, int attempt, TestContext setupModel) {
        // Create a new TestPhaseExceptionModel instance with the provided exception.
        TestPhaseExceptionModel exceptionModel = new TestPhaseExceptionModel(exception);

        // Set the attempt reference, context properties, and display name for the exception model.
        exceptionModel
                .setAttemptReference(attempt)
                .setContext(setupModel.getEnvironment().getContextProperties())
                .setDisplayName(TestContext.getTestDisplayName());

        // If the WebDriver is null, return the exception model without adding a screenshot.
        if (setupModel.getWebDriver() == null) {
            return exceptionModel;
        }

        // Capture a screenshot from the WebDriver and encode it as a Base64 string.
        String screenshot = ((TakesScreenshot) setupModel.getWebDriver()).getScreenshotAs(OutputType.BASE64);

        // Add the screenshot to the exception model.
        exceptionModel.setScreenshot(screenshot);

        // Return the populated exception model.
        return exceptionModel;
    }
}
