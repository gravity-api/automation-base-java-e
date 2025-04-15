package io.automation.ui.pages;

import io.automation.engine.core.PageModelBase;
import io.automation.engine.core.TestContext;
import io.automation.ui.handlers.ButtonHandler;
import io.automation.ui.handlers.TextboxHandler;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPageModel extends PageModelBase {
    /**
     * Constructs a `PageModelBase` instance with the given `TestContext`.
     * This constructor delegates to the other constructor, passing `null` as the URI.
     *
     * @param testContext the `TestContext` containing configuration and WebDriver information
     */
    public LoginPageModel(TestContext testContext) {
        super(testContext);
    }

    /**
     * Constructs a `PageModelBase` instance with the given `TestContext` and optional URI.
     * If a non-null URI is provided, the WebDriver navigates to the specified URI.
     *
     * @param testContext the `TestContext` containing configuration and WebDriver information
     * @param uri         the URI to navigate to, or `null` if no navigation is required
     */
    public LoginPageModel(TestContext testContext, String uri) {
        super(testContext, uri);
    }

    // TODO: Check why @Step is not working here
    @Step("Perform login with username: {username} and password: {password}")
    public LoginPageModel login(String username, String password) {
        Allure.step("Perform login with username: " + username + " and password: " + password);

        // Initialize the handler for interacting with textboxes
        TextboxHandler textboxHandler = new TextboxHandler(getTestContext());

        // Initialize the handler for interacting with buttons
        ButtonHandler buttonHandler = new ButtonHandler(getTestContext());

        // Enter the username into the "Username" textbox
        textboxHandler.getElement("Username").sendKeys(username);

        // Enter the password into the "Password" textbox
        textboxHandler.getElement("Password").sendKeys(password);

        // Click the "Login" button to submit the login form
        buttonHandler.getElement("Login").submit();

        // Return the current instance of LoginPageModel for method chaining
        return this;
    }

    // TODO: Check why @Step is not working here
    @Step("Confirm successful login by checking for the presence of the logout link")
    public LoginPageModel confirmLogin() {
        // Log the confirmation step
        Allure.step("Confirm successful login by checking for the presence of the logout link");

        // Define the XPath for the logout link
        String xpath = "//a[contains(@href,'/logout')]";
        By locator = By.xpath(xpath);

        // Wait until the logout link is visible on the page
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(locator));

        // Log the successful confirmation of the login
        TestContext.AllureUtility.newAllureScreenshot("LoginConfirmationScreenshot", getWebDriver());

        // Return the current instance of LoginPageModel for method chaining
        return this;
    }
}
