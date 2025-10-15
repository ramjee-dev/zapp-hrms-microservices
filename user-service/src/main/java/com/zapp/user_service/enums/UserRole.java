package com.zapp.user_service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum UserRole {
    ADMIN("ADMIN", "Administrator"),
    BD("BD", "Business Development"),
    TAT("TAT", "Talent Acquisition Team"),
    HR("HR", "Human Resources"),
    MANAGER("MANAGER", "Manager"),
    USER("USER", "Standard User");

    private final String code;
    private final String displayName;

    UserRole(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * Returns internal code of the enum.
     */
    @JsonValue
    public String getCode() {
        return code;
    }

    /**
     * Returns human-readable display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Lookup enum by code (case-insensitive).
     */
    @JsonCreator
    public static UserRole fromCode(String code) {
        return Arrays.stream(UserRole.values())
                .filter(role -> role.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Unknown UserRole code: " + code));
    }

    @Override
    public String toString() {
        return displayName;
    }
}
