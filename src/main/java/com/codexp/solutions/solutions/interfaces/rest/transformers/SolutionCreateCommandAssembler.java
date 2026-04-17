package com.codexp.solutions.solutions.interfaces.rest.transformers;

import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import com.codexp.solutions.solutions.domain.model.commands.CreateSolutionCommand;
import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.ChallengeId;
import com.codexp.solutions.solutions.domain.model.valueobjects.TemplateLanguage;
import com.codexp.solutions.solutions.interfaces.rest.requests.CreateSolutionRequest;

public class SolutionCreateCommandAssembler {

    public static CreateSolutionCommand toCreateSolutionCommand(
        CreateSolutionRequest request,
        String requesterId,
        UserRole requesterRole
    ) {
        return new CreateSolutionCommand(
            ChallengeId.fromString(request.challengeId()),
            AuthorId.fromString(requesterId),
            requesterRole,
            TemplateLanguage.fromString(request.language())
        );
    }
}
