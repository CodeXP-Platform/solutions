package com.codexp.solutions.shared.domain.model.valueobjects;

public record UserId(String value) {

    public static UserId fromString(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be blank or empty.");
        }

        return new UserId(userId.trim());
    }

    @Override
    public String toString() {
        return value;
    }
}
