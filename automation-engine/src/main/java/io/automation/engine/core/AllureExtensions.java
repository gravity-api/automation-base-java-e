package io.automation.engine.core;

import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

import java.util.UUID;

public class AllureExtensions {
    /**
     * Executes a step in the AllureExtensions report with the given name and action.
     * The step is marked as passed if the action completes successfully,
     * or as failed if an exception is thrown during execution.
     *
     * @param stepName the name of the step to be displayed in the AllureExtensions report
     * @param action   the action to be executed within the step
     * @throws Throwable if the action throws any exception, it is propagated
     */
    public static void throwableStep(String stepName, ThrowingRunnable action) throws Throwable {
        // Generate a unique identifier for the step.
        String uuid = UUID.randomUUID().toString();

        // Create a new StepResult with the given name.
        StepResult stepResult = new StepResult().setName(stepName);

        // Start the step using the lifecycle API.
        io.qameta.allure.Allure.getLifecycle().startStep(uuid, stepResult);
        try {
            // Execute the step action.
            action.run();

            // Mark the step as passed.
            io.qameta.allure.Allure.getLifecycle().updateStep(uuid, sr -> sr.setStatus(Status.PASSED));
        } catch (Throwable t) {
            // Mark the step as failed.
            io.qameta.allure.Allure.getLifecycle().updateStep(uuid, sr -> sr.setStatus(Status.FAILED));

            // Propagate the exception.
            throw t;
        } finally {
            // Stop the step.
            io.qameta.allure.Allure.getLifecycle().stopStep(uuid);
        }
    }

    /**
     * Functional interface to allow throwing a Throwable.
     */
    @SuppressWarnings("squid:S112")
    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Throwable;
    }
}
