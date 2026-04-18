package com.codexp.solutions.solutions.application.queryservices;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codexp.solutions.solutions.domain.model.Attempt;
import com.codexp.solutions.solutions.domain.model.queries.GetLatestAttemptBySolutionIdQuery;
import com.codexp.solutions.solutions.domain.services.AttemptQueryService;
import com.codexp.solutions.solutions.infrastructure.persistence.jpa.repositories.AttemptRepository;

@Service
public class AttemptQueryServiceImpl implements AttemptQueryService {

    private final AttemptRepository attemptRepository;

    public AttemptQueryServiceImpl(AttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    @Override
    public Optional<Attempt> handle(GetLatestAttemptBySolutionIdQuery query) {
        return attemptRepository.findTopBySolutionIdOrderByCreatedAtDesc(query.solutionId());
    }
}
