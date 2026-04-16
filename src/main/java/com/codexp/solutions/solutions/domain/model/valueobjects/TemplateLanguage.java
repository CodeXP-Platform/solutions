package com.codexp.solutions.solutions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record TemplateLanguage(String value) {
    public static final int MAX_LENGTH = 30;

    public static TemplateLanguage fromString(String language) {
        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException("Template language cannot be blank or empty.");
        }

        String normalized = language.trim().toLowerCase();

        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Template language cannot exceed %d characters.", MAX_LENGTH)
            );
        }

        return new TemplateLanguage(normalized);
    }

    @Override
    public String toString() {
        return value;
    }
}
