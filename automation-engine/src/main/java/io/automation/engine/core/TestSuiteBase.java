package io.automation.engine.core;

import io.automation.engine.exceptions.TestSetupException;
import io.automation.engine.exceptions.TestTeardownException;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;

import java.util.Map;

/**
 * Base class for test suites in the automation framework.
 * This class provides lifecycle hooks for setup and teardown phases
 * at the suite and test levels. It also manages environment properties
 * for the test suite.
 */
public abstract class TestSuiteBase {
    // A map to store environment properties for the test suite, initialized during construction.
    private final Map<String, Object> environmentProperties;

    /**
     * Constructor for the `TestSuiteBase` class.
     * This initializes the environment properties for the test suite by loading them
     * from the `.env` file associated with the test suite's class.
     */
    protected TestSuiteBase() {
        // Load environment properties from the `.env` file and assign them to the environmentProperties map.
        environmentProperties = AssemblyInitialize.getGlobalEnvironment();
    }

    /**
     * Retrieves the environment properties for the test suite.
     * These properties are loaded during the initialization of the test suite
     * and can be used to configure or access environment-specific settings.
     *
     * @return A map containing the environment properties.
     */
    public Map<String, Object> getEnvironmentProperties() {
        // Return the map of environment properties initialized during the constructor.
        return environmentProperties;
    }

    /**
     * This method is executed before each test in the test suite.
     * It performs any necessary setup actions for individual cases.
     * Subclasses can override the `onTestSetup` method to implement custom setup logic.
     */
    @BeforeEach
    @SuppressWarnings({"squid:S112"}) // Suppresses warnings about generic exceptions.
    public void testSetup() {
        Allure.step("JUnit Test Setup Phase", ()->{
            try {
                // Invoke the custom setup logic defined in the subclass.
                onTestSetup(environmentProperties);
            } catch (Exception e) {
                // Wrap the exception in a TestSetupException with a descriptive message.
                TestSetupException testSetupException = new TestSetupException("Failed to setup test", e.getCause());

                // Rethrow the exception as a RuntimeException to fail the test.
                throw new RuntimeException(testSetupException);
            }
        });
    }

    /**
     * This method is called after each test execution to perform any necessary teardown actions.
     * It invokes the `onTestTeardown` method, which can be overridden by subclasses to implement
     * custom teardown logic for individual cases. If an exception occurs during teardown, it is
     * wrapped in a `TestTeardownException` and rethrown as a `RuntimeException`.
     */
    @AfterEach
    @SuppressWarnings({"squid:S112"})
    public void testTeardown() {
        Allure.step("JUnit Test Teardown Phase", () -> {
            try {
                // Invoke the custom teardown logic defined in the subclass.
                onTestTeardown(environmentProperties);
            } catch (Exception e) {
                // Wrap the exception in a TestTeardownException with a descriptive message.
                TestTeardownException testTeardownException =
                        new TestTeardownException("Failed to teardown test", e.getCause());

                // Rethrow the exception as a RuntimeException to fail the test.
                throw new RuntimeException(testTeardownException);
            }
        });
    }

    /**
     * This method is called during the setup phase of the test suite.
     * Subclasses can override this method to implement custom setup logic
     * for the entire test suite, such as initializing resources or configuring
     * environment properties.
     *
     * @param environmentProperties A map containing the environment properties for the test context.
     */
    protected void onSetup(Map<String, Object> environmentProperties) {
        // Default implementation does nothing. Override to add custom setup logic.
    }

    /**
     * This method is called during the teardown phase of the test suite.
     * Subclasses can override this method to implement custom teardown logic
     * for the entire test suite, such as releasing resources or logging information.
     *
     * @param environmentProperties A map containing the environment properties for the test context.
     */
    protected void onTeardown(Map<String, Object> environmentProperties) {
        // Default implementation does nothing. Override to add custom teardown logic.
    }

    /**
     * This method is called before each test execution to perform any necessary setup actions.
     * Subclasses can override this method to implement custom setup logic for individual cases.
     *
     * @param environmentProperties A map containing the environment properties for the test context.
     */
    protected void onTestSetup(Map<String, Object> environmentProperties) {
        // Default implementation does nothing. Override to add custom setup logic.
    }

    /**
     * This method is called after each test execution to perform any necessary teardown actions.
     * Subclasses can override this method to implement custom teardown logic for individual cases.
     *
     * @param environmentProperties A map containing the environment properties for the test context.
     */
    protected void onTestTeardown(Map<String, Object> environmentProperties) {
        // Default implementation does nothing. Override to add custom teardown logic.
    }
}
