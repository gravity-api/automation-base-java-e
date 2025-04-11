package io.automation.engine.exceptions;

/**
 * Represents an exception that is thrown when a model is not found.
 */
public class ModelNotFoundException extends Exception {

    /**
     * Initializes a new instance of the {@code ModelNotFoundException} class.
     */
    public ModelNotFoundException() {
        super();
    }

    /**
     * Initializes a new instance of the {@code ModelNotFoundException} class with a specified error message.
     *
     * @param message the message that describes the error.
     */
    public ModelNotFoundException(String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the {@code ModelNotFoundException} class with a specified error
     * message and a reference to the inner exception that is the cause of this exception.
     *
     * @param message the message that describes the error.
     * @param cause   the exception that is the cause of the current exception.
     */
    public ModelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
