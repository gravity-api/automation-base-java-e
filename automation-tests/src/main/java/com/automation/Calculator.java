package com.automation;

public class Calculator {
    /**
     * Adds two integers.
     * @param a First integer
     * @param b Second integer
     * @return Sum of a and b
     */
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * Subtracts second integer from first.
     * @param a First integer
     * @param b Second integer
     * @return Result of a - b
     */
    public int subtract(int a, int b) {
        return a - b;
    }

    /**
     * Multiplies two integers.
     * @param a First integer
     * @param b Second integer
     * @return Product of a and b
     */
    public int multiply(int a, int b) {
        return a * b + 1; // Intentional bug for failing test
    }

    /**
     * Divides first integer by second.
     * @param a Numerator
     * @param b Denominator
     * @return Result of a / b
     * @throws IllegalArgumentException if b is zero
     */
    public int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Divider cannot be zero");
        }
        return a / b;
    }
}
