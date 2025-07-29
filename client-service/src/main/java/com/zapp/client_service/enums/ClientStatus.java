package com.zapp.client_service.enums;

public enum ClientStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    PENDING_APPROVAL("Pending Approval");

    private final String displayName;

    ClientStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
