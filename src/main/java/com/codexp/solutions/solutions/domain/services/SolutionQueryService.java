package com.codexp.solutions.solutions.domain.services;

import com.codexp.solutions.solutions.domain.model.Solution;
import com.codexp.solutions.solutions.domain.model.queries.GetSolutionByIdQuery;
import com.codexp.solutions.solutions.domain.model.queries.GetSolutionByChallengeQuery;

public interface SolutionQueryService {
    Solution handle(GetSolutionByIdQuery query);

    Solution handle(GetSolutionByChallengeQuery query);
}
