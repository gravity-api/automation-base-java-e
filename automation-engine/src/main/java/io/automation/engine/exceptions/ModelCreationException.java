package io.automation.engine.exceptions;

/**
 * Represents an exception that is thrown when there is an error during model creation.
 */
public class ModelCreationException extends RuntimeException {
    /**
     * Initializes a new instance of the {@code ModelCreationException} class.
     */
    public ModelCreationException() {
        super();
    }

    /**
     * Initializes a new instance of the {@code ModelCreationException} class with a specified error message.
     *
     * @param message the message that describes the error.
     */
    public ModelCreationException(String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the {@code ModelCreationException} class with a specified error
     * message and a reference to the inner exception that is the cause of this exception.
     *
     * @param message the message that describes the error.
     * @param cause   the exception that is the cause of the current exception.
     */
    public ModelCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
