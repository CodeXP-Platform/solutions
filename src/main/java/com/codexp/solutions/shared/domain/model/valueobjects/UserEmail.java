package com.codexp.solutions.shared.domain.model.valueobjects;

public record UserEmail(String value) {

    public static UserEmail fromString(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank or empty.");
        }

        String normalized = email.trim();
        if (!normalized.contains("@")) {
            throw new IllegalArgumentException("Email is invalid.");
        }

        return new UserEmail(normalized);
    }

    @Override
    public String toString() {
        return value;
    }
}
