package io.automation.examples.cases;

import io.automation.engine.core.TestCaseBase;
import io.automation.engine.core.TestContext;
import io.automation.examples.app.Calculator;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

public class C0002 extends TestCaseBase {
    @Override
    // Use this approach if your test has only one step and does not involve any model usage
    @Step("Verify subtraction operation in Calculator")
    protected void automationTest(TestContext testContext) {
        Calculator calculator = new Calculator();
        Assertions.assertEquals(1, calculator.subtract(3, 2), "3 - 2 should equal 1");
    }
}
