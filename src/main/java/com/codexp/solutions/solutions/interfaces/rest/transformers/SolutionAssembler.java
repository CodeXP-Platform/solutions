package com.codexp.solutions.solutions.interfaces.rest.transformers;

import com.codexp.solutions.solutions.domain.model.Attempt;
import com.codexp.solutions.solutions.domain.model.Solution;
import com.codexp.solutions.solutions.interfaces.rest.responses.SolutionResponse;
import com.codexp.solutions.solutions.interfaces.rest.responses.SubmitSolutionResponse;

public class SolutionAssembler {

    public static SolutionResponse toResponse(Solution solution) {
        return new SolutionResponse(
            solution.getId().toString(),
            solution.getChallengeId().toString(),
            solution.getCodeTemplateId().toString(),
            solution.getAuthorId().toString(),
            solution.getLanguage().toString(),
            solution.getCode().toString(),
            solution.getStatus().name(),
            solution.getMaxAttempts().value(),
            solution.getCurrentAttempts().value(),
            solution.remainingAttempts(),
            solution.getAttemptsResetAt(),
            solution.getUpdatedAt(),
            solution.getCreatedAt()
        );
    }

    public static SubmitSolutionResponse toSubmitResponse(Solution solution) {
        return new SubmitSolutionResponse(
            solution.getId().toString(),
            solution.getStatus().name(),
            solution.getCurrentAttempts().value(),
            solution.getMaxAttempts().value(),
            solution.remainingAttempts(),
            solution.getAttemptsResetAt()
        );
    }
}
