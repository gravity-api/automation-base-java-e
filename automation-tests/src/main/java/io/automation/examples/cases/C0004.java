package io.automation.examples.cases;

import io.automation.engine.core.TestCaseBase;
import io.automation.engine.core.TestContext;
import io.automation.examples.app.Calculator;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

public class C0004 extends TestCaseBase {
    @Override
    // Use this approach if your test has only one step and does not involve any model usage
    @Step("Verify division operation in Calculator")
    protected void automationTest(TestContext testContext) {
        Calculator calculator = new Calculator();
        Assertions.assertThrows(IllegalArgumentException.class, ()
                -> calculator.divide(10, 0), "Dividing by zero should throw exception");
    }
}
