package com.zapp.client_service.enums;

public enum ClientType {
    ENTERPRISE("Enterprise"),
    CORPORATE("Corporate"),
    SMB("Small/Medium Business"),
    STARTUP("Startup");

    private final String displayName;

    ClientType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
