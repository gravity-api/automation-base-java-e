package io.automation.engine.exceptions;

/**
 * Exception that is thrown when there is an error during test teardown.
 */
public class TestTeardownException extends Exception {

    /**
     * Initializes a new instance of the {@code TestTeardownException} class.
     */
    public TestTeardownException() {
        super();
    }

    /**
     * Initializes a new instance of the {@code TestTeardownException} class with a specified error message.
     *
     * @param message the message that describes the error.
     */
    public TestTeardownException(String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the {@code TestTeardownException} class with a specified error message and a reference
     * to the cause of this exception.
     *
     * @param message the message that describes the error.
     * @param cause   the exception that is the cause of the current exception, or {@code null} if no cause is specified.
     */
    public TestTeardownException(String message, Throwable cause) {
        super(message, cause);
    }
}
