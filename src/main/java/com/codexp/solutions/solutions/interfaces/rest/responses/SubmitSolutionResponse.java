package com.codexp.solutions.solutions.interfaces.rest.responses;

import java.time.Instant;

public record SubmitSolutionResponse(
    String solutionId,
    String attemptId,
    String status,
    int currentAttempts,
    int maxAttempts,
    int remainingAttempts,
    Instant attemptsResetAt
) {}
