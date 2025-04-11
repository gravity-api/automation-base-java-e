package io.automation.engine.models;

import java.time.Duration;
import java.time.LocalDateTime;

public class TestPhaseResultModel {
    private Duration duration;
    private LocalDateTime endTime;
    private TestPhaseExceptionModel phaseException;
    private boolean isSuccess;
    private String phaseName;
    private LocalDateTime startTime;

    /**
     * Gets the duration of the test phase.
     *
     * @return the duration as a {@link Duration}
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the test phase.
     *
     * @param duration the duration to set
     */
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    /**
     * Gets the end time of the test phase.
     *
     * @return the end time as a {@link LocalDateTime}
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the test phase.
     *
     * @param endTime the end time to set
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the exception details if the test phase encountered an error.
     *
     * @return the exception details as a {@link TestPhaseExceptionModel}
     */
    public TestPhaseExceptionModel getPhaseException() {
        return phaseException;
    }

    /**
     * Sets the exception details if the test phase encountered an error.
     *
     * @param phaseException the exception details to set
     */
    public void setPhaseException(TestPhaseExceptionModel phaseException) {
        this.phaseException = phaseException;
    }

    /**
     * Indicates whether the test phase was successful.
     *
     * @return {@code true} if the test phase was successful; otherwise, {@code false}
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Sets whether the test phase was successful.
     *
     * @param isSuccess {@code true} if successful; otherwise, {@code false}
     */
    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * Gets the name of the test phase.
     *
     * @return the test phase name
     */
    public String getPhaseName() {
        return phaseName;
    }

    /**
     * Sets the name of the test phase.
     *
     * @param phaseName the test phase name to set
     */
    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    /**
     * Gets the start time of the test phase.
     *
     * @return the start time as a {@link LocalDateTime}
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the test phase.
     *
     * @param startTime the start time to set
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
