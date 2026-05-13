package com.codexp.solutions.solutions.interfaces.rest.responses;

import java.time.Instant;

public record SolutionResponse(
    String solutionId,
    String challengeId,
    String codeTemplateId,
    String authorId,
    String language,
    String code,
    String status,
    int maxAttempts,
    int currentAttempts,
    int remainingAttempts,
    Instant attemptsResetAt,
    Instant updatedAt,
    Instant createdAt
) {}
