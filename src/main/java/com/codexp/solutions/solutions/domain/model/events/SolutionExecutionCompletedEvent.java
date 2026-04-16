package com.codexp.solutions.solutions.domain.model.events;

import java.time.Instant;
import java.util.List;

public record SolutionExecutionCompletedEvent(
    String eventId,
    String eventType,
    Instant timestamp,
    Data data
) {
    public record Data(
        String solutionId,
        Boolean isSuccessful,
        Long totalExecutionTimeMs,
        String globalError,
        List<TestResult> testResults
    ) {}

    public record TestResult(
        String testId,
        Boolean passed,
        Boolean isHidden,
        String actualOutput,
        String expectedOutput
    ) {}
}
