package io.automation.examples.cases;

import io.automation.engine.core.TestCaseBase;
import io.automation.engine.core.TestContext;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

public class C123456 extends TestCaseBase {
    @Override
    protected void onSetup(TestContext testContext) {
        super.onSetup(testContext);
    }

    @Override
    protected void onTeardown(TestContext testContext) {
        super.onTeardown(testContext);
    }

    @Override
    protected void automationTest(TestContext testContext) {
        WebDriver driver = testContext.getWebDriver();

        Allure.step("Open the browser and navigate to the URL");
        driver.get("https://example.com");

        Allure.step("Get the title of the page");
        String title = driver.getTitle();

        Allure.step("Verify the title of the page", () -> {
            TestContext.AllureUtility.newAllureScreenshot("Screenshot after test", driver);
            Assertions.assertEquals("Example Domain", title, "Title does not match");
        });

        TestContext.AllureUtility.newAllureAttachment("Attachment after test", "This is an attachment");
    }
}
