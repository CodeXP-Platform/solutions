package com.codexp.solutions.solutions.domain.model.valueobjects;

import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public record ChallengeId(UUID value) {

    public static ChallengeId fromString(String challengeId) {
        if (challengeId == null || challengeId.isBlank()) {
            throw new IllegalArgumentException("Challenge ID cannot be blank or empty");
        }

        try {
            return new ChallengeId(UUID.fromString(challengeId.trim()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Challenge ID format. Must be a valid UUID.", e);
        }
    }

    public static ChallengeId fromUUID(UUID challengeId) {
        return new ChallengeId(challengeId);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
