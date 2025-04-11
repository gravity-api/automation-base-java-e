package io.automation.engine.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestResultModel {
    private int attemptNumber;
    private Map<String, Object> auditableActions;
    private String displayName;
    private List<TestPhaseExceptionModel> exceptions;
    private Map<String, Object> metrics;
    private boolean passed;
    private Duration findElementsDuration;
    private String runId;
    private Duration setupDuration;
    private LocalDateTime setupEndTime;
    private LocalDateTime setupStartTime;
    private Duration teardownDuration;
    private LocalDateTime teardownEndTime;
    private LocalDateTime teardownStartTime;
    private String testClassName;
    private LocalDateTime testEndTime;
    private String testMethodName;
    private LocalDateTime testStartTime;
    private Duration testDuration;

    /**
     * Gets the current attempt number for executing the test case.
     *
     * @return the attempt number.
     */
    public int getAttemptNumber() {
        return attemptNumber;
    }

    /**
     * Sets the current attempt number for executing the test case.
     *
     * @param attemptNumber the attempt number to set.
     */
    public void setAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    /**
     * Gets the dictionary of auditable actions and their corresponding metrics.
     *
     * @return a map where the key is the action name and the value is the metric.
     */
    public Map<String, Object> getAuditableActions() {
        return auditableActions;
    }

    /**
     * Sets the dictionary of auditable actions and their corresponding metrics.
     *
     * @param auditableActions the auditable actions map to set.
     */
    public void setAuditableActions(Map<String, Object> auditableActions) {
        this.auditableActions = auditableActions;
    }

    /**
     * Gets the display name of the test case.
     *
     * @return the test case display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name of the test case.
     *
     * @param displayName the display name to set.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the collection of exceptions that occurred during the test case.
     *
     * @return a list of exception details.
     */
    public List<TestPhaseExceptionModel> getExceptions() {
        return exceptions;
    }

    /**
     * Sets the collection of exceptions that occurred during the test case.
     *
     * @param exceptions the list of exceptions to set.
     */
    public void setExceptions(List<TestPhaseExceptionModel> exceptions) {
        this.exceptions = exceptions;
    }

    /**
     * Gets the dictionary of metrics related to the test case.
     *
     * @return a map where the key is the metric name and the value is the metric value.
     */
    public Map<String, Object> getMetrics() {
        return metrics;
    }

    /**
     * Sets the dictionary of metrics related to the test case.
     *
     * @param metrics the metrics map to set.
     */
    public void setMetrics(Map<String, Object> metrics) {
        this.metrics = metrics;
    }

    /**
     * Checks if the test case passed.
     *
     * @return {@code true} if the test case passed; otherwise, {@code false}.
     */
    public boolean isPassed() {
        return passed;
    }

    /**
     * Sets the pass status of the test case.
     *
     * @param passed {@code true} if the test case passed; otherwise, {@code false}.
     */
    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    /**
     * Gets the duration taken to find elements during the test case.
     *
     * @return the duration for finding elements.
     */
    public Duration getFindElementsDuration() {
        return findElementsDuration;
    }

    /**
     * Sets the duration taken to find elements during the test case.
     *
     * @param findElementsDuration the duration to set.
     */
    public void setFindElementsDuration(Duration findElementsDuration) {
        this.findElementsDuration = findElementsDuration;
    }

    /**
     * Gets the unique identifier for the current test run.
     *
     * @return the run identifier.
     */
    public String getRunId() {
        return runId;
    }

    /**
     * Sets the unique identifier for the current test run.
     *
     * @param runId the run identifier to set.
     */
    public void setRunId(String runId) {
        this.runId = runId;
    }

    /**
     * Gets the duration taken to set up the test case.
     *
     * @return the setup duration.
     */
    public Duration getSetupDuration() {
        return setupDuration;
    }

    /**
     * Sets the duration taken to set up the test case.
     *
     * @param setupDuration the setup duration to set.
     */
    public void setSetupDuration(Duration setupDuration) {
        this.setupDuration = setupDuration;
    }

    /**
     * Gets the end time of the setup phase.
     *
     * @return the setup end time.
     */
    public LocalDateTime getSetupEndTime() {
        return setupEndTime;
    }

    /**
     * Sets the end time of the setup phase.
     *
     * @param setupEndTime the setup end time to set.
     */
    public void setSetupEndTime(LocalDateTime setupEndTime) {
        this.setupEndTime = setupEndTime;
    }

    /**
     * Gets the start time of the setup phase.
     *
     * @return the setup start time.
     */
    public LocalDateTime getSetupStartTime() {
        return setupStartTime;
    }

    /**
     * Sets the start time of the setup phase.
     *
     * @param setupStartTime the setup start time to set.
     */
    public void setSetupStartTime(LocalDateTime setupStartTime) {
        this.setupStartTime = setupStartTime;
    }

    /**
     * Gets the duration taken to tear down the test case.
     *
     * @return the teardown duration.
     */
    public Duration getTeardownDuration() {
        return teardownDuration;
    }

    /**
     * Sets the duration taken to tear down the test case.
     *
     * @param teardownDuration the teardown duration to set.
     */
    public void setTeardownDuration(Duration teardownDuration) {
        this.teardownDuration = teardownDuration;
    }

    /**
     * Gets the end time of the teardown phase.
     *
     * @return the teardown end time.
     */
    public LocalDateTime getTeardownEndTime() {
        return teardownEndTime;
    }

    /**
     * Sets the end time of the teardown phase.
     *
     * @param teardownEndTime the teardown end time to set.
     */
    public void setTeardownEndTime(LocalDateTime teardownEndTime) {
        this.teardownEndTime = teardownEndTime;
    }

    /**
     * Gets the start time of the teardown phase.
     *
     * @return the teardown start time.
     */
    public LocalDateTime getTeardownStartTime() {
        return teardownStartTime;
    }

    /**
     * Sets the start time of the teardown phase.
     *
     * @param teardownStartTime the teardown start time to set.
     */
    public void setTeardownStartTime(LocalDateTime teardownStartTime) {
        this.teardownStartTime = teardownStartTime;
    }

    /**
     * Gets the name of the test class.
     *
     * @return the test class name.
     */
    public String getTestClassName() {
        return testClassName;
    }

    /**
     * Sets the name of the test class.
     *
     * @param testClassName the test class name to set.
     */
    public void setTestClassName(String testClassName) {
        this.testClassName = testClassName;
    }

    /**
     * Gets the end time of the test execution phase.
     *
     * @return the test end time.
     */
    public LocalDateTime getTestEndTime() {
        return testEndTime;
    }

    /**
     * Sets the end time of the test execution phase.
     *
     * @param testEndTime the test end time to set.
     */
    public void setTestEndTime(LocalDateTime testEndTime) {
        this.testEndTime = testEndTime;
    }

    /**
     * Gets the name of the test method.
     *
     * @return the test method name.
     */
    public String getTestMethodName() {
        return testMethodName;
    }

    /**
     * Sets the name of the test method.
     *
     * @param testMethodName the test method name to set.
     */
    public void setTestMethodName(String testMethodName) {
        this.testMethodName = testMethodName;
    }

    /**
     * Gets the start time of the test execution phase.
     *
     * @return the test start time.
     */
    public LocalDateTime getTestStartTime() {
        return testStartTime;
    }

    /**
     * Sets the start time of the test execution phase.
     *
     * @param testStartTime the test start time to set.
     */
    public void setTestStartTime(LocalDateTime testStartTime) {
        this.testStartTime = testStartTime;
    }

    /**
     * Gets the duration taken to execute the test case.
     *
     * @return the test execution duration.
     */
    public Duration getTestDuration() {
        return testDuration;
    }

    /**
     * Sets the duration taken to execute the test case.
     *
     * @param testDuration the test execution duration to set.
     */
    public void setTestDuration(Duration testDuration) {
        this.testDuration = testDuration;
    }

    /**
     * Updates the setup phase results of the test result.
     *
     * @param setupResult the setup phase result to use for updating.
     * @return the updated {@link TestResultModel}.
     */
    public TestResultModel updateSetupResults(TestPhaseResultModel setupResult) {
        // Check if the phase name is "Setup"
        if (!"Setup".equalsIgnoreCase(setupResult.getPhaseName())) {
            return this;
        }

        // Update the setup start time, end time, and duration.
        setupStartTime = setupResult.getStartTime();
        setupEndTime = setupResult.getEndTime();
        setupDuration = setupResult.getDuration();

        // Collect exceptions from the setupResult if any.
        List<TestPhaseExceptionModel> newExceptions = new ArrayList<>();
        if (setupResult.getPhaseException() != null) {
            newExceptions.add(setupResult.getPhaseException());
        }

        // Ensure the exceptions list in testResult is initialized.
        if (exceptions == null) {
            exceptions = new ArrayList<>();
        }

        // Add new exceptions to the existing ones.
        exceptions.addAll(newExceptions);

        // Return the updated test result.
        return this;
    }

    /**
     * Updates the teardown phase results of the test result.
     *
     * @param teardownResult the teardown phase result to use for updating.
     * @return the updated {@link TestResultModel}.
     */
    public TestResultModel updateTeardownResults(TestPhaseResultModel teardownResult) {
        // Check if the phase name is "Teardown"
        if (!"Teardown".equalsIgnoreCase(teardownResult.getPhaseName())) {
            return this;
        }

        // Update the teardown start time, end time, and duration.
        teardownStartTime = teardownResult.getStartTime();
        teardownEndTime = teardownResult.getEndTime();
        teardownDuration = teardownResult.getDuration();

        // Collect exceptions if there are any.
        List<TestPhaseExceptionModel> newExceptions = new ArrayList<>();
        if (teardownResult.getPhaseException() != null) {
            newExceptions.add(teardownResult.getPhaseException());
        }

        // Ensure the exceptions list in testResult is initialized.
        if (exceptions == null) {
            exceptions = new ArrayList<>();
        }

        // Append new exceptions to the existing exceptions.
        exceptions.addAll(newExceptions);

        // Return the updated test result.
        return this;
    }
}
