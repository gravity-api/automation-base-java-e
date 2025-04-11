package io.automation.engine.tests;

import io.automation.engine.core.TestCaseBase;
import io.automation.engine.models.AutomationEnvironment;
import io.automation.engine.core.TestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class TestCaseBaseTests {
    @Test
    void InitializeEnvironmentTest() {
        // Initialize the environment by creating an instance of TestCaseDemo.
        AutomationEnvironment environment = new TestCaseDemo().getEnvironment();

        // Retrieve the context properties from the initialized environment.
        Map<String, Object> contextProperties = environment.getContextProperties();

        // Retrieve the URL property for the AUT (Application Under Test) from the context properties.
        String autUrl = contextProperties.get("URL").toString();

        // Assert that the retrieved URL matches the expected value.
        Assertions.assertEquals("http://example.com", autUrl);
    }

    /**
     * A demo implementation of the abstract `TestCaseBase` class.
     * This class is used for testing purposes to verify the behavior of the `TestCaseBase` functionality.
     */
    private static class TestCaseDemo extends TestCaseBase {
        /**
         * Constructor for `TestCaseDemo`.
         * Calls the parent constructor to initialize the environment.
         */
        public TestCaseDemo() {
            super();
        }

        @Override
        protected void automationTest(TestContext setupModel) {
            // This method is intentionally left empty for testing purposes.
        }
    }
}
