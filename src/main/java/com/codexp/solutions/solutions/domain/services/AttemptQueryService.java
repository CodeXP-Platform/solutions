package com.codexp.solutions.solutions.domain.services;

import java.util.List;
import java.util.Optional;

import com.codexp.solutions.solutions.domain.model.Attempt;
import com.codexp.solutions.solutions.domain.model.queries.GetAttemptsBySolutionIdQuery;
import com.codexp.solutions.solutions.domain.model.queries.GetLatestAttemptBySolutionIdQuery;

public interface AttemptQueryService {
    Optional<Attempt> handle(GetLatestAttemptBySolutionIdQuery query);
    List<Attempt> handle(GetAttemptsBySolutionIdQuery query);
}
