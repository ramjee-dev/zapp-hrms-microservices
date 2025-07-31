package com.zapp.candidate_service.enums;

/**
 * Standardized experience levels for candidates and job requisitions.
 */
public enum ExperienceLevel {
    ENTRY_LEVEL("Entry Level"),
    JUNIOR("Junior"),
    MID_LEVEL("Mid Level"),
    SENIOR("Senior"),
    LEAD("Lead"),
    PRINCIPAL("Principal"),
    EXECUTIVE("Executive");

    private final String displayName;

    ExperienceLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}