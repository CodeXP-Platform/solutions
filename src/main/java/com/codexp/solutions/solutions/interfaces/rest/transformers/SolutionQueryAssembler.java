package com.codexp.solutions.solutions.interfaces.rest.transformers;

import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import com.codexp.solutions.solutions.domain.model.queries.GetSolutionByIdQuery;
import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionId;

public class SolutionQueryAssembler {

    public static GetSolutionByIdQuery toGetSolutionByIdQuery(
        String solutionId,
        String requesterId,
        UserRole requesterRole
    ) {
        return new GetSolutionByIdQuery(
            SolutionId.fromString(solutionId),
            AuthorId.fromString(requesterId),
            requesterRole
        );
    }
}
