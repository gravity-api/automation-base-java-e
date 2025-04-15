package io.automation.ui.handlers;

import io.automation.engine.core.TestContext;
import io.automation.engine.core.UiControllerHandlerBase;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ButtonHandler extends UiControllerHandlerBase {
    /**
     * Constructor for the UiControllerHandlerBase class.
     * Initializes the base class with the provided test context.
     *
     * @param testContext The test context to be used by this handler.
     */
    public ButtonHandler(TestContext testContext) {
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
    @Step("Searching for button with label: {query}")
    protected WebElement onGetElement(String query) {
        // Define the XPath template for locating a button with the exact label
        String template = "//button[.='%s']";

        // Format the XPath with the provided query (button label)
        String xpath = String.format(template, query);

        // Create a locator using the formatted XPath
        By locator = By.xpath(xpath);

        // Wait until the element located by the XPath is visible and return it
        return getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
