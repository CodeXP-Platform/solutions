package com.codexp.solutions.solutions.interfaces.rest.transformers;

import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import com.codexp.solutions.solutions.domain.model.queries.GetAttemptsBySolutionIdQuery;
import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionId;

public class AttemptQueryAssembler {
    public static GetAttemptsBySolutionIdQuery toGetAttemptsBySolutionIdQuery(
        String solutionId,
        String requesterId,
        UserRole requesterRole
    ) {
        return new GetAttemptsBySolutionIdQuery(
            SolutionId.fromString(solutionId),
            AuthorId.fromString(requesterId),
            requesterRole
        );
    }
}
