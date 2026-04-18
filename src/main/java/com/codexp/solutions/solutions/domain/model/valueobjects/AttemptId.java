package com.codexp.solutions.solutions.domain.model.valueobjects;

import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public record AttemptId(UUID value) {

    public static AttemptId fromString(String attemptId) {
        if (attemptId == null || attemptId.isBlank()) {
            throw new IllegalArgumentException("Attempt ID cannot be blank or empty");
        }

        try {
            return new AttemptId(UUID.fromString(attemptId.trim()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Attempt ID format. Must be a valid UUID.", e);
        }
    }

    public static AttemptId generate() {
        return new AttemptId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
