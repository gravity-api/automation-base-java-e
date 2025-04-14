package io.automation.examples.suites;

import io.automation.engine.core.TestSuiteBase;
import io.automation.engine.models.TestResultModel;
import io.automation.examples.cases.C123456;
import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;

import java.util.Map;

@Story("The user story name from Jira")
@DisplayName("Expressive name of the test suite")
class UserStoryTests extends TestSuiteBase {
    @Override
    protected void onSetup(Map<String, Object> environmentProperties) {
        // Add a key-value pair to the environment properties map during setup.
        // This can be used to initialize or configure the environment before the test suite execution.
        environmentProperties.put("FromSetup", "FooBar");
    }

    @Override
    protected void onTeardown(Map<String, Object> environmentProperties) {
        // Add a key-value pair to the environment properties map during teardown.
        // This can be used to clean up or log information after the test suite execution.
        environmentProperties.put("FromTeardown", "FooBar");
    }

    @Override
    protected void onTestSetup(Map<String, Object> environmentProperties) {
        Allure.step("Test Setup Step", ()-> {
            // This step is executed before each test case in the test suite.
            // You can add any setup logic here that needs to be executed for each test case.
            // For example, you can initialize WebDriver or set up test data.
        });
    }

    @Override
    protected void onTestTeardown(Map<String, Object> environmentProperties) {
        environmentProperties.put("FromTestTeardown", "FooBar");
    }

    @Test
    @Tag("UserStory")
    @Tag("Sanity")
    @Tag("System")
    @DisplayName("Some expression name")
    void TGPP123456Test() {
        // Create an instance of the test case class
        C123456 testCase = new C123456();

        // Set the test case's environment properties to the environment properties from the test suite
        // This allows the test case to access the same environment properties as the test suite
        // The test case can use these properties for its own setup or execution
        // You can add the value to any key in the environment properties map such as
        // context properties, test data, or test parameters
        testCase
                .getEnvironment()
                .getContextProperties()
                .put("FromSetup", getEnvironmentProperties().get("FromSetup"));

        // Invoke the test case and get the result
        TestResultModel result = testCase.invoke();

        // Assert the result of the test case
        Assertions.assertTrue(result.isPassed());
    }
}
