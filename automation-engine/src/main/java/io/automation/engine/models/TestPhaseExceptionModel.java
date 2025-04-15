package io.automation.engine.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Describes a contract for receiving test exception data.
 */
public class TestPhaseExceptionModel {
    private int attemptReference;
    private Map<String, Object> context;
    private String displayName;
    private Throwable exception;
    private String reasonPhrase;
    private String screenshot;

    /**
     * Default constructor.
     */
    public TestPhaseExceptionModel() {
        this(null);
    }

    /**
     * Initializes a new instance of the TestPhaseExceptionModel class with the provided exception.
     *
     * @param exception an Exception related to the TestPhaseExceptionModel.
     */
    public TestPhaseExceptionModel(Throwable exception) {
        // Initialize the context dictionary.
        this.context = new HashMap<>();

        // Set the exception.
        this.exception = exception;

        // Compute the reason phrase: if exception is null, then empty string; otherwise, use the base exception's message.
        this.reasonPhrase = (exception == null)
                ? ""
                : getBaseException(exception).getMessage();
    }

    /**
     * Gets or sets the reference number for the attempt that caused the exception.
     *
     * @return the attempt reference.
     */
    public int getAttemptReference() {
        return attemptReference;
    }

    /**
     * Sets the reference number for the attempt that caused the exception.
     *
     * @param attemptReference the attempt reference to set.
     */
    public TestPhaseExceptionModel setAttemptReference(int attemptReference) {
        // Assign the provided attempt reference to the instance variable.
        this.attemptReference = attemptReference;

        // Return the current instance to allow method chaining.
        return this;
    }

    /**
     * Gets or sets the context dictionary for additional exception data.
     *
     * @return the context map.
     */
    public Map<String, Object> getContext() {
        return context;
    }

    /**
     * Sets the context dictionary for additional exception data.
     *
     * @param context the context map to set.
     */
    public TestPhaseExceptionModel setContext(Map<String, Object> context) {
        // Assign the provided context map to the instance variable.
        this.context = context;

        // Return the current instance to allow method chaining.
        return this;
    }

    /**
     * Gets or sets the display name for the exception.
     *
     * @return the display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name for the exception.
     *
     * @param displayName the display name to set.
     */
    public TestPhaseExceptionModel setDisplayName(String displayName) {
        // Assign the provided display name to the instance variable.
        this.displayName = displayName;

        // Return the current instance to allow method chaining.
        return this;
    }

    /**
     * Gets or sets the Exception object that was thrown.
     *
     * @return the exception.
     */
    public Throwable getException() {
        return exception;
    }

    /**
     * Sets the Exception object that was thrown.
     *
     * @param exception the exception to set.
     */
    public TestPhaseExceptionModel setException(Exception exception) {
        // Assign the provided exception to the instance variable.
        this.exception = exception;

        // Return the current instance to allow method chaining.
        return this;
    }

    /**
     * Gets or sets the reason phrase for the exception model.
     *
     * @return the reason phrase.
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    /**
     * Sets the reason phrase for the exception model.
     *
     * @param reasonPhrase the reason phrase to set.
     */
    public TestPhaseExceptionModel setReasonPhrase(String reasonPhrase) {
        // Assign the provided reason phrase to the instance variable.
        this.reasonPhrase = reasonPhrase;

        // Return the current instance to allow method chaining.
        return this;
    }

    /**
     * Gets or sets the screenshot in base64 format.
     *
     * @return the screenshot in base64.
     */
    public String getScreenshot() {
        return screenshot;
    }

    /**
     * Sets the screenshot in base64 format.
     *
     * @param screenshot the screenshot in base64 format to set.
     */
    public TestPhaseExceptionModel setScreenshot(String screenshot) {
        // Assign the provided screenshot (in base64 format) to the instance variable.
        this.screenshot = screenshot;

        // Return the current instance to allow method chaining.
        return this;
    }

    // Recursively retrieves the base exception from the provided exception.
    private static Throwable getBaseException(Throwable exception) {
        // Start with the provided exception
        Throwable current = exception;

        // Traverse the chain of causes until the root cause is found
        while (current.getCause() != null) {
            current = current.getCause();
        }

        // Return the root cause exception
        return current;
    }
}
