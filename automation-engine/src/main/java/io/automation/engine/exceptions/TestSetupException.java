package io.automation.engine.exceptions;

/**
 * Exception that is thrown when there is an error during test setup.
 */
public class TestSetupException extends Exception {

    /**
     * Initializes a new instance of the {@code TestSetupException} class.
     */
    public TestSetupException() {
        super();
    }

    /**
     * Initializes a new instance of the {@code TestSetupException} class with a specified error message.
     *
     * @param message the message that describes the error.
     */
    public TestSetupException(String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the {@code TestSetupException} class with a specified error message and a
     * reference to the inner exception that is the cause of this exception.
     *
     * @param message the message that describes the error.
     * @param cause   the exception that is the cause of the current exception, or {@code null} if no cause is specified.
     */
    public TestSetupException(String message, Throwable cause) {
        super(message, cause);
    }
}
