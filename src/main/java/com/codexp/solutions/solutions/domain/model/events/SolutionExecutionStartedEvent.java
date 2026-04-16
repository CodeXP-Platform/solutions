package com.codexp.solutions.solutions.domain.model.events;

import java.time.Instant;

public record SolutionExecutionStartedEvent(
    String eventId,
    String eventType,
    Instant timestamp,
    Data data
) {
    public record Data(String solutionId) {}
}
