package com.codexp.solutions.solutions.domain.services;

import com.codexp.solutions.solutions.domain.model.Solution;
import com.codexp.solutions.solutions.domain.model.queries.GetSolutionByChallengeQuery;
import com.codexp.solutions.solutions.domain.model.queries.GetSolutionByIdQuery;
import com.codexp.solutions.solutions.domain.model.queries.GetSolutionsByChallengeQuery;
import java.util.List;

public interface SolutionQueryService {
    Solution handle(GetSolutionByIdQuery query);

    Solution handle(GetSolutionByChallengeQuery query);

    List<Solution> handle(GetSolutionsByChallengeQuery query);
}
