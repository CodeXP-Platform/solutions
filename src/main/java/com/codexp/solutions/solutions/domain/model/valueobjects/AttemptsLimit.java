package com.codexp.solutions.solutions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record AttemptsLimit(int value) {

    public static AttemptsLimit fromInt(Integer maxAttempts) {
        if (maxAttempts == null || maxAttempts <= 0) {
            throw new IllegalArgumentException("Max attempts must be greater than 0.");
        }

        return new AttemptsLimit(maxAttempts);
    }
}
