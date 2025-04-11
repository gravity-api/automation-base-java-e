package io.automation.examples.cases;

import io.automation.engine.core.TestCaseBase;
import io.automation.engine.core.TestContext;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;

public class CGPP123456 extends TestCaseBase {
    @Override
    protected void onSetup(TestContext setupModel) {
        super.onSetup(setupModel);
    }

    @Override
    protected void onTeardown(TestContext setupModel) {
        super.onTeardown(setupModel);
    }

    @Override
    protected void automationTest(TestContext setupModel) {
        WebDriver driver = setupModel.getWebDriver();

        Allure.step("Open the browser and navigate to the URL", () -> {
            driver.get("https://example.com");
        });

        Allure.step("Verify the title of the page", () -> {
            String title = driver.getTitle();
            Assertions.assertEquals("Example Domain", title, "Title does not match");
        });

        TestContext.AllureUtility.newAllureScreenshot("Screenshot after test", driver);
        TestContext.AllureUtility.newAllureAttachment("Attachment after test", "This is an attachment");
    }
}
