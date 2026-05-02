package com.codexp.solutions.solutions.domain.model.valueobjects;

import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public record CodeTemplateId(UUID value) {

    public static CodeTemplateId fromString(String codeTemplateId) {
        if (codeTemplateId == null || codeTemplateId.isBlank()) {
            throw new IllegalArgumentException("Code Template ID cannot be blank or empty");
        }

        try {
            return new CodeTemplateId(UUID.fromString(codeTemplateId.trim()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Code Template ID format. Must be a valid UUID.", e);
        }
    }

    public static CodeTemplateId fromUUID(UUID codeTemplateId) {
        return new CodeTemplateId(codeTemplateId);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
