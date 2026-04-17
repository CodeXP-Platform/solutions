package com.codexp.solutions.solutions.domain.model.events;

import java.time.Instant;

public record SolutionRequestedEvent(
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
        String challengeId,
        String authorId,
        String language,
        String templateCode
    ) {
        public String challengeId() {
            return challengeId;
        }

        public String authorId() {
            return authorId;
        }

        public String language() {
            return language;
        }

        public String templateCode() {
            return templateCode;
        }
    }
}
