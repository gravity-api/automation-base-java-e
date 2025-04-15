package io.automation.examples.cases;

import io.automation.engine.core.TestCaseBase;
import io.automation.engine.core.TestContext;
import io.automation.ui.pages.LoginPageModel;

import org.junit.jupiter.api.Assertions;

import java.util.Objects;

public class C0005 extends TestCaseBase {
    @Override
    protected void automationTest(TestContext testContext) {
        // Retrieve the page URI from the context properties, defaulting to "about:blank" if not found
        String pageUri = testContext
                .getEnvironment()
                .getContextProperties()
                .getOrDefault("PRACTICE_URL", "about:blank")
                .toString();

        // Initialize the LoginPageModel with the test context and the retrieved page URI
        LoginPageModel loginPage = new LoginPageModel(testContext, pageUri);

        // Perform login with predefined credentials and confirm the login
        loginPage.login("practice", "SuperSecretPassword!").confirmLogin();

        // Verify that the current URL matches the expected "secure" pattern
        boolean expected =
                Objects.requireNonNull(testContext.getWebDriver().getCurrentUrl()).matches(".*secure.*");
        String message =
                "Login failed, expected URL to contain 'secure' but got: " + testContext.getWebDriver().getCurrentUrl();
        Assertions.assertTrue(expected, message);
    }
}
