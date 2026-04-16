package com.codexp.solutions.solutions.interfaces.rest.transformers;

import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import com.codexp.solutions.solutions.domain.model.commands.SubmitSolutionCommand;
import com.codexp.solutions.solutions.domain.model.commands.UpdateSolutionCodeCommand;
import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionCode;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionId;
import com.codexp.solutions.solutions.interfaces.rest.requests.UpdateSolutionCodeRequest;

public class SolutionCommandAssembler {

    public static UpdateSolutionCodeCommand toUpdateSolutionCodeCommand(
        String solutionId,
        UpdateSolutionCodeRequest request,
        String userId,
        UserRole userRole
    ) {
        return new UpdateSolutionCodeCommand(
            SolutionId.fromString(solutionId),
            AuthorId.fromString(userId),
            userRole,
            SolutionCode.fromString(request.code())
        );
    }

    public static SubmitSolutionCommand toSubmitSolutionCommand(
        String solutionId,
        String userId,
        UserRole userRole
    ) {
        return new SubmitSolutionCommand(
            SolutionId.fromString(solutionId),
            AuthorId.fromString(userId),
            userRole
        );
    }
}
