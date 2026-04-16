package com.codexp.solutions.solutions.domain.services;

import com.codexp.solutions.solutions.domain.model.Solution;
import com.codexp.solutions.solutions.domain.model.queries.GetSolutionByIdQuery;

public interface SolutionQueryService {
    Solution handle(GetSolutionByIdQuery query);
}
