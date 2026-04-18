package com.codexp.solutions.solutions.domain.model.events;

import java.time.Instant;

public record SolutionExecutionStartedEvent(
    String eventId,
    String eventType,
    Instant timestamp,
    Data data
) {
    public String eventId() {
        return eventId;
    }

    public String eventType() {
        return eventType;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public Data data() {
        return data;
    }

    public record Data(
        String solutionId,
        String executionId,
        Instant startedAt
    ) {
        public String solutionId() {
            return solutionId;
        }

        public String executionId() {
            return executionId;
        }

        public Instant startedAt() {
            return startedAt;
        }
    }
}
