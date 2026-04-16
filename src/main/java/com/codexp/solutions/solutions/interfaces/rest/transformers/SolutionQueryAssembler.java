package com.codexp.solutions.solutions.interfaces.rest.transformers;

import com.codexp.solutions.solutions.domain.model.queries.GetSolutionByIdQuery;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionId;

public class SolutionQueryAssembler {

    public static GetSolutionByIdQuery toGetSolutionByIdQuery(String solutionId) {
        return new GetSolutionByIdQuery(SolutionId.fromString(solutionId));
    }
}
