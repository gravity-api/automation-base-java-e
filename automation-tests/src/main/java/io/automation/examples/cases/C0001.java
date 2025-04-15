package io.automation.examples.cases;

import io.automation.engine.core.TestCaseBase;
import io.automation.engine.core.TestContext;
import io.automation.examples.app.Calculator;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

public class C0001 extends TestCaseBase {
    /**
     * Abstract method to define the main automation test logic.
     * Subclasses must implement this method to provide the specific test steps
     * to be executed during the test case.
     *
     * @param testContext the {@link TestContext} instance containing the test's context and resources.
     */
    @Override
    // Use this approach if your test has only one step and does not involve any model usage
    @Step("Verify addition operation in Calculator")
    protected void automationTest(TestContext testContext) {
        Calculator calculator = new Calculator();
        Assertions.assertEquals(4, calculator.add(2, 3), "2 + 3 should equal 5");
    }
}
