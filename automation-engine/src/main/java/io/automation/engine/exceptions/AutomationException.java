package io.automation.engine.exceptions;

/**
 * Represents a base exception for automation-related errors.
 */
public class AutomationException extends RuntimeException {
    /**
     * Initializes a new instance of the {@code AutomationException} class.
     */
    public AutomationException() {
        super();
    }

    /**
     * Initializes a new instance of the {@code AutomationException} class with a specified error message.
     *
     * @param message the message that describes the error.
     */
    public AutomationException(String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the {@code AutomationException} class with a specified error
     * message and a reference to the inner exception that is the cause of this exception.
     *
     * @param message the message that describes the error.
     * @param cause   the exception that is the cause of the current exception.
     */
    public AutomationException(String message, Throwable cause) {
        super(message, cause);
    }
}
