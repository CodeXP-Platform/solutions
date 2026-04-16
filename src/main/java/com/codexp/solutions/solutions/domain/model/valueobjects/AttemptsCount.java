package com.codexp.solutions.solutions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record AttemptsCount(int value) {

    public static AttemptsCount fromInt(Integer currentAttempts) {
        if (currentAttempts == null || currentAttempts < 0) {
            throw new IllegalArgumentException("Current attempts cannot be negative.");
        }

        return new AttemptsCount(currentAttempts);
    }

    public static AttemptsCount zero() {
        return new AttemptsCount(0);
    }

    public AttemptsCount increment() {
        return new AttemptsCount(value + 1);
    }
}
