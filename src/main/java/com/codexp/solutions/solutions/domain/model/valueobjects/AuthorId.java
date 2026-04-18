package com.codexp.solutions.solutions.domain.model.valueobjects;

import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public record AuthorId(UUID value) {

    public static AuthorId fromString(String authorId) {
        if (authorId == null || authorId.isBlank()) {
            throw new IllegalArgumentException("Author ID cannot be blank or empty.");
        }

        try {
            return new AuthorId(UUID.fromString(authorId.trim()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Author ID format. Must be a valid UUID.", e);
        }
    }

    public static AuthorId fromUUID(UUID authorId) {
        return new AuthorId(authorId);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
