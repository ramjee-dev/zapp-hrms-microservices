package com.zapp.candidate_service.enums;

/**
 * Represents the lifecycle status of a candidate in the recruiting pipeline.
 */
public enum CandidateStatus {
    APPLIED("Applied"),
    SCREENING("Screening"),
    INTERVIEW_SCHEDULED("Interview Scheduled"),
    INTERVIEW_CONDUCTED("Interview Conducted"),
    TECHNICAL_ASSESSMENT("Technical Assessment"),
    SECOND_INTERVIEW("Second Interview"),
    REFERENCE_CHECK("Reference Check"),
    BACKGROUND_CHECK("Background Check"),
    OFFER_EXTENDED("Offer Extended"),
    OFFER_ACCEPTED("Offer Accepted"),
    OFFER_DECLINED("Offer Declined"),
    SELECTED("Selected"),
    REJECTED("Rejected"),
    WITHDRAWN("Withdrawn"),
    ON_HOLD("On Hold");

    private final String displayName;

    CandidateStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}