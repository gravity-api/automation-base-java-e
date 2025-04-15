package io.automation.examples.suites;

import io.automation.engine.core.TestSuiteBase;

import io.automation.examples.cases.C0001;
import io.automation.examples.cases.C0002;
import io.automation.examples.cases.C0003;
import io.automation.examples.cases.C0004;
import org.junit.jupiter.api.*;

import java.util.Map;

/**
 * Unit cases for the Calculator class.
 */
@SuppressWarnings("squid:S5960")
@DisplayName("Calculator Unit Tests")
public class CalculatorTest extends TestSuiteBase {
    // Sets up the test environment by initializing environment properties.
    // This method is called before the execution of the test suite.
    @Override
    protected void onTestSetup(Map<String, Object> environmentProperties) {
        // Set the remote address for the driver (empty for now)
        environmentProperties.put("DRIVER_REMOTE_ADDRESS", "");

        // Set the type of driver to be used (empty for now)
        environmentProperties.put("DRIVER_TYPE", "");
    }

    /**
     * Test addition operation.
     */
    @Test
    @Tag("fast")
    @Tag("unit")
    @DisplayName("Test Addition")
    void t0001() {
        new C0001().invoke();
    }

    /**
     * Test subtraction operation.
     */
    @Test
    @Tag("fast")
    @Tag("unit")
    @DisplayName("Test Subtraction")
    void t0002() {
        new C0002().invoke();
    }

    /**
     * Test multiplication operation.
     * This test is skipped for demonstration purposes.
     */
    @Test
    @Tag("slow")
    @Tag("integration")
    @Disabled("Skipping multiply test for demonstration purposes")
    @DisplayName("Test Multiplication")
    void t0003() {
        new C0003().invoke();
    }

    /**
     * Test division operation, especially division by zero.
     */
    @Test
    @Tag("fast")
    @Tag("unit")
    @DisplayName("Test Division")
    void t0004() {
        new C0004().invoke();
    }
}
