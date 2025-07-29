package com.zapp.job_service.enums;

public enum JobStatus {

    DRAFT("Draft"),
    PUBLISHED("Published"),
    ON_HOLD("On Hold"),
    CLOSED("Closed"),
    CANCELLED("Cancelled");

    private final String displayName;

    JobStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
