package com.codexp.solutions.solutions.domain.model.valueobjects;

public record AttemptWindowMinutes(int value) {

    public static AttemptWindowMinutes fromInt(int windowMinutes) {
        if (windowMinutes <= 0) {
            throw new IllegalArgumentException("Attempt reset window must be greater than 0 minutes.");
        }

        return new AttemptWindowMinutes(windowMinutes);
    }
}
