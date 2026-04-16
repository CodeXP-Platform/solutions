package com.codexp.solutions.solutions.application.queryservices;

import org.springframework.stereotype.Service;

import com.codexp.solutions.solutions.domain.exceptions.SolutionNotFoundException;
import com.codexp.solutions.solutions.domain.model.Solution;
import com.codexp.solutions.solutions.domain.model.queries.GetSolutionByIdQuery;
import com.codexp.solutions.solutions.domain.services.SolutionQueryService;
import com.codexp.solutions.solutions.infrastructure.persistence.jpa.repositories.SolutionRepository;

@Service
public class SolutionQueryServiceImpl implements SolutionQueryService {

    private final SolutionRepository solutionRepository;

    public SolutionQueryServiceImpl(SolutionRepository solutionRepository) {
        this.solutionRepository = solutionRepository;
    }

    @Override
    public Solution handle(GetSolutionByIdQuery query) {
        return solutionRepository.findById(query.solutionId()).orElseThrow(SolutionNotFoundException::new);
    }
}
