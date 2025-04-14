package io.automation.engine.core;

import com.google.common.base.Stopwatch;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for UI controller handlers.
 * This class provides common functionality for handling UI interactions
 * and managing test context during automated tests.
 */
@SuppressWarnings("unused")
public abstract class UiControllerHandlerBase extends ModelBase {
    /**
     * Constructor for the UiControllerHandlerBase class.
     * Initializes the base class with the provided test context.
     *
     * @param testContext The test context to be used by this handler.
     */
    protected UiControllerHandlerBase(TestContext testContext) {
        // Call the superclass constructor to initialize the base model with the test context.
        super(testContext);
    }

    /**
     * Retrieves a WebElement based on the provided query.
     * This method tracks the duration of the element search and updates the test context with the elapsed time.
     *
     * @param query The query string used to locate the WebElement.
     * @return The WebElement found using the query.
     */
    public WebElement getElement(String query) {
        // Define a key for storing the duration of element searches in the test context.
        final String DURATION_KEY = "findElementsDuration";

        // Retrieve the current duration of element searches from the test context.
        long findElementsDuration = (long) getTestContext()
                .getEnvironment()
                .getTestData()
                .getOrDefault(DURATION_KEY, 0L);

        // Start a stopwatch to measure the duration of the element search.
        Stopwatch stopwatch = Stopwatch.createStarted();

        try {
            // Delegate the actual element retrieval to the abstract method.
            return onGetElement(query);
        } finally {
            // Stop the stopwatch and calculate the elapsed time.
            stopwatch.stop();

            // Add the elapsed time to the total duration of element searches.
            findElementsDuration += stopwatch.elapsed().toNanos();

            // Update the test context with the new total duration.
            getTestContext().getEnvironment().getTestData().put(DURATION_KEY, findElementsDuration);
        }
    }

    /**
     * Retrieves a list of WebElements based on the provided query.
     * This method tracks the duration of the element search and updates the test context with the elapsed time.
     *
     * @param query The query string used to locate the WebElements.
     * @return A list of WebElements found using the query.
     */
    public List<WebElement> getElements(String query) {
        // Define a key for storing the duration of element searches in the test context.
        final String DURATION_KEY = "findElementsDuration";

        // Retrieve the current duration of element searches from the test context.
        long findElementsDuration = (long) getTestContext()
                .getEnvironment()
                .getTestData()
                .getOrDefault(DURATION_KEY, 0L);

        // Start a stopwatch to measure the duration of the element search.
        Stopwatch stopwatch = Stopwatch.createStarted();

        try {
            // Delegate the actual element retrieval to the abstract method.
            return onGetElements(query);
        } finally {
            // Stop the stopwatch and calculate the elapsed time.
            stopwatch.stop();

            // Add the elapsed time to the total duration of element searches.
            findElementsDuration += stopwatch.elapsed().toNanos();

            // Update the test context with the new total duration.
            getTestContext().getEnvironment().getTestData().put(DURATION_KEY, findElementsDuration);
        }
    }

    /**
     * Abstract method to retrieve a WebElement based on the provided query.
     * Subclasses must implement this method to define the logic for locating elements.
     *
     * @param query The query string used to locate the WebElement.
     * @return The WebElement found using the query.
     */
    protected abstract WebElement onGetElement(String query);

    /**
     * Retrieves a list of WebElements based on the provided query.
     * This method is not implemented in the base class and should be
     * overridden by subclasses to define the logic for locating multiple elements.
     *
     * @param query The query string used to locate the WebElements.
     * @return A list of WebElements found using the query.
     */
    protected List<WebElement> onGetElements(String query) {
        // This method is not implemented in the base class.
        // Subclasses should provide their own implementation if needed.
        return new ArrayList<>();
    }
}
