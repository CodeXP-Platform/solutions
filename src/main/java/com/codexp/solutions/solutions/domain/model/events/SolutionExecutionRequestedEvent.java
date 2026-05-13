package com.codexp.solutions.solutions.domain.model.events;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SolutionExecutionRequestedEvent(
    UUID eventId,
    String eventType,
    Instant timestamp,
    Data data
) {
    public static SolutionExecutionRequestedEvent create(
        String solutionId,
        String attemptId,
        String challengeId,
        String userId,
        String language,
        String entryFunctionName,
        String code,
        List<TestCaseData> testCases
    ) {
        return new SolutionExecutionRequestedEvent(
            UUID.randomUUID(),
            "SolutionExecutionRequestedEvent",
            Instant.now(),
            new Data(
                solutionId,
                attemptId,
                challengeId,
                userId,
                language,
                entryFunctionName,
                code,
                testCases
            )
        );
    }

    public record Data(
        String solutionId,
        String attemptId,
        String challengeId,
        String userId,
        String language,
        String entryFunctionName,
        String code,
        List<TestCaseData> testCases
    ) {}

    public record TestCaseData(
        String testId,
        String input,
        String expectedOutput,
        Boolean isHidden
    ) {}
}
