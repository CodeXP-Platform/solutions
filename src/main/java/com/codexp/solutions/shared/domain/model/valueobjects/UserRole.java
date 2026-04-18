package com.codexp.solutions.shared.domain.model.valueobjects;

import java.util.Locale;

public enum UserRole {
    ROLE_STUDENT,
    ROLE_TEACHER,
    ROLE_ADMIN;

    public static UserRole fromClaim(String rawRole) {
        if (rawRole == null || rawRole.isBlank()) {
            throw new IllegalArgumentException("Role claim is missing");
        }

        String normalized = rawRole.trim().toUpperCase(Locale.ROOT);
        if (!normalized.startsWith("ROLE_")) {
            normalized = "ROLE_" + normalized;
        }

        return UserRole.valueOf(normalized);
    }

    public String asAuthority() {
        return name();
    }
}
