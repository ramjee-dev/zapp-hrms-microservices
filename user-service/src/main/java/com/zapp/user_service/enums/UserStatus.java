package com.zapp.user_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum UserStatus {
    PENDING_ACTIVATION("PENDING_ACTIVATION", "Pending Activation"),
    ACTIVE("ACTIVE", "Active"),
    INACTIVE("INACTIVE", "Inactive"),
    SUSPENDED("SUSPENDED", "Suspended"),
    LOCKED("LOCKED", "Locked"),
    EXPIRED("EXPIRED", "Expired");

    private final String code;
    private final String displayName;

    UserStatus(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * Returns the internal code of the enum (used for DB storage, logs).
     */
    @JsonValue
    public String getCode() {
        return code;
    }

    /**
     * Returns the friendly display name for UI.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Case-insensitive lookup by code, to convert from DB or incoming API data to enum.
     * Throws NoSuchElementException if not found, to fail fast on invalid values.
     */
    @JsonCreator
    public static UserStatus fromCode(String code) {
        return Arrays.stream(UserStatus.values())
                .filter(status -> status.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Unknown UserStatus code: " + code));
    }

    @Override
    public String toString() {
        return displayName;
    }
}
