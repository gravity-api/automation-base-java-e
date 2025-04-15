package io.automation.ui.handlers;

import io.automation.engine.core.TestContext;
import io.automation.engine.core.UiControllerHandlerBase;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TextboxHandler extends UiControllerHandlerBase {
    /**
     * Constructor for the UiControllerHandlerBase class.
     * Initializes the base class with the provided test context.
     *
     * @param testContext The test context to be used by this handler.
     */
    public TextboxHandler(TestContext testContext) {
        super(testContext);
    }

    /**
     * Abstract method to retrieve a WebElement based on the provided query.
     * Subclasses must implement this method to define the logic for locating elements.
     *
     * @param query The query string used to locate the WebElement.
     * @return The WebElement found using the query.
     */
    @Override
    @Step("Searching for textbox with label: {query}")
    protected WebElement onGetElement(String query) {
        // Define an XPath template to locate a text input field based on its associated label
        String template = "//div[./input[@type='text' or @type='password'] and ./label[.='%s']]/input";

        // Format the XPath template with the provided query (label text)
        String xpath = String.format(template, query);

        // Create a locator using the formatted XPath
        By locator = By.xpath(xpath);

        // Wait until the element located by the XPath is visible and return it
        return getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
