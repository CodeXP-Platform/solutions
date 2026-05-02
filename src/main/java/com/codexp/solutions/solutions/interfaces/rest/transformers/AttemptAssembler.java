package com.codexp.solutions.solutions.interfaces.rest.transformers;

import com.codexp.solutions.solutions.domain.model.Attempt;
import com.codexp.solutions.solutions.interfaces.rest.responses.AttemptResponse;

public class AttemptAssembler {
    public static AttemptResponse toResponse(Attempt attempt) {
        return new AttemptResponse(
            attempt.getId().value().toString(),
            attempt.getStatus().name(),
            attempt.getExecutionTimeMs(),
            attempt.getErrorDetails(),
            attempt.failedTestIdsView(),
            attempt.getCreatedAt()
        );
    }
}
