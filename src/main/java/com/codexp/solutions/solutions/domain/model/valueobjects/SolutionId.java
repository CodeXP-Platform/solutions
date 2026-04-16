package com.codexp.solutions.solutions.domain.model.valueobjects;

import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public record SolutionId(UUID value) {

    public static SolutionId fromString(String solutionId) {
        if (solutionId == null || solutionId.isBlank()) {
            throw new IllegalArgumentException("Solution ID cannot be blank or empty");
        }

        try {
            return new SolutionId(UUID.fromString(solutionId.trim()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Solution ID format. Must be a valid UUID.", e);
        }
    }

    public static SolutionId generate() {
        return new SolutionId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
