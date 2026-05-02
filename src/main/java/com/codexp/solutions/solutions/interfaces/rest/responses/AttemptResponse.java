package com.codexp.solutions.solutions.interfaces.rest.responses;

import java.time.Instant;
import java.util.List;

public record AttemptResponse(
    String id,
    String status,
    Long executionTimeMs,
    String errorDetails,
    List<String> failedTestIds,
    Instant createdAt
) {}
