package com.automation;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;

/**
 * Unit cases for the Calculator class.
 */
@DisplayName("Calculator Unit Tests")
public class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        Allure.step("Test Setup Phase", () -> calculator = new Calculator());
    }

    /**
     * Test addition operation.
     */
    @Test
    @Tag("fast")
    @Tag("unit")
    @DisplayName("Test Addition")
    void testAdd() {
        Allure.step("Test Execution Phase", () -> {
            Assertions.assertEquals(4, calculator.add(2, 3), "2 + 3 should equal 5");
        });
    }

    /**
     * Test subtraction operation.
     */
    @Test
    @Tag("fast")
    @Tag("unit")
    @DisplayName("Test Subtraction")
    void testSubtract() {
        Allure.step("Test Execution Phase", () -> {
            Assertions.assertEquals(1, calculator.subtract(3, 2), "3 - 2 should equal 1");
        });
    }

    /**
     * Test multiplication operation.
     * This test is skipped for demonstration purposes.
     */
    @Test
    @Tag("slow")
    @Tag("integration")
    @Disabled("Skipping multiply test for demonstration purposes")
    @DisplayName("Test Multiplication")
    void testMultiply() {
        Allure.step("Test Execution Phase", () -> {
            Assertions.assertEquals(6, calculator.multiply(2, 3), "2 * 3 should equal 6");
        });
    }

    /**
     * Test division operation, especially division by zero.
     */
    @Test
    @Tag("fast")
    @Tag("unit")
    @DisplayName("Test Division")
    void testDivide() {
        Allure.step("Test Execution Phase", () -> {
            Allure.step("this is a nested step");
            Assertions.assertThrows(IllegalArgumentException.class, () -> calculator.divide(10, 0), "Dividing by zero should throw exception");
        });
    }

    @AfterEach
    public void teardown() {
        Allure.step("Test Teardown Phase", () -> {
            // Your teardown code here.
        });
    }
}
