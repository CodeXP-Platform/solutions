package com.codexp.solutions.solutions.domain.model.events;

import java.time.Instant;

public record SolutionRequestedEvent(
    String eventId,
    String eventType,
    Instant timestamp,
    Data data
) {
    public record Data(
        String challengeId,
        String authorId,
        String language,
        String templateCode
    ) {}
}
