package com.codexp.solutions.solutions.domain.model.events;

import java.time.Instant;
import java.util.List;

public record SolutionExecutionCompletedEvent(
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
        Boolean isSuccessful,
        Long totalExecutionTimeMs,
        String globalError,
        List<TestResult> testResults
    ) {
        public String solutionId() {
            return solutionId;
        }

        public String executionId() {
            return executionId;
        }

        public Boolean isSuccessful() {
            return isSuccessful;
        }

        public Long totalExecutionTimeMs() {
            return totalExecutionTimeMs;
        }

        public String globalError() {
            return globalError;
        }

        public List<TestResult> testResults() {
            return testResults;
        }
    }

    public record TestResult(
        String testId,
        Boolean passed,
        Boolean isHidden,
        String actualOutput,
        String expectedOutput,
        Long executionTimeMs,
        String errorMessage
    ) {
        public String testId() {
            return testId;
        }

        public Boolean passed() {
            return passed;
        }

        public Boolean isHidden() {
            return isHidden;
        }

        public String actualOutput() {
            return actualOutput;
        }

        public String expectedOutput() {
            return expectedOutput;
        }

        public Long executionTimeMs() {
            return executionTimeMs;
        }

        public String errorMessage() {
            return errorMessage;
        }
    }
}
