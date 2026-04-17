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
        String language,
        String entryFunctionName,
        String code,
        List<TestCaseData> testCases
    ) {
        return new SolutionExecutionRequestedEvent(
            UUID.randomUUID(),
            "SolutionExecutionRequestedEvent",
            Instant.now(),
            new Data(solutionId, language, entryFunctionName, code, testCases)
        );
    }

    public UUID eventId() {
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
        String language,
        String entryFunctionName,
        String code,
        List<TestCaseData> testCases
    ) {
        public String solutionId() {
            return solutionId;
        }

        public String language() {
            return language;
        }

        public String entryFunctionName() {
            return entryFunctionName;
        }

        public String code() {
            return code;
        }

        public List<TestCaseData> testCases() {
            return testCases;
        }
    }

    public record TestCaseData(
        String testId,
        String input,
        String expectedOutput,
        Boolean isHidden
    ) {
        public String testId() {
            return testId;
        }

        public String input() {
            return input;
        }

        public String expectedOutput() {
            return expectedOutput;
        }

        public Boolean isHidden() {
            return isHidden;
        }
    }
}
