package com.codexp.solutions.solutions.interfaces.rest.responses;

import java.time.Instant;
import java.util.List;

public record SolutionResponse(
    String solutionId,
    String challengeId,
    String authorId,
    String language,
    String code,
    String status,
    int maxAttempts,
    int currentAttempts,
    int remainingAttempts,
    Instant attemptsResetAt,
    Long executionTimeMs,
    String errorDetails,
    List<String> failedTestIds,
    Instant updatedAt,
    Instant createdAt
) {}
