package com.codexp.solutions.solutions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record SolutionCode(String value) {
    public static final int MAX_LENGTH = 100_000;

    public static SolutionCode fromString(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Solution code cannot be blank or empty.");
        }

        if (code.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Solution code cannot exceed %d characters.", MAX_LENGTH)
            );
        }

        return new SolutionCode(code);
    }

    @Override
    public String toString() {
        return value;
    }
}
