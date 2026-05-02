package com.codexp.solutions.solutions.application.queryservices;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codexp.solutions.shared.domain.exceptions.UnauthorizedActionException;
import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import com.codexp.solutions.solutions.domain.exceptions.SolutionNotFoundException;
import com.codexp.solutions.solutions.domain.exceptions.SolutionOwnershipException;
import com.codexp.solutions.solutions.domain.model.Attempt;
import com.codexp.solutions.solutions.domain.model.Solution;
import com.codexp.solutions.solutions.domain.model.queries.GetAttemptsBySolutionIdQuery;
import com.codexp.solutions.solutions.domain.model.queries.GetLatestAttemptBySolutionIdQuery;
import com.codexp.solutions.solutions.domain.services.AttemptQueryService;
import com.codexp.solutions.solutions.infrastructure.persistence.jpa.repositories.AttemptRepository;
import com.codexp.solutions.solutions.infrastructure.persistence.jpa.repositories.SolutionRepository;

@Service
public class AttemptQueryServiceImpl implements AttemptQueryService {

    private final AttemptRepository attemptRepository;
    private final SolutionRepository solutionRepository;

    public AttemptQueryServiceImpl(AttemptRepository attemptRepository, SolutionRepository solutionRepository) {
        this.attemptRepository = attemptRepository;
        this.solutionRepository = solutionRepository;
    }

    @Override
    public Optional<Attempt> handle(GetLatestAttemptBySolutionIdQuery query) {
        return attemptRepository.findTopBySolutionIdOrderByCreatedAtDesc(query.solutionId());
    }

    @Override
    public List<Attempt> handle(GetAttemptsBySolutionIdQuery query) {
        if (!UserRole.ROLE_STUDENT.equals(query.requesterRole()) && !UserRole.ROLE_ADMIN.equals(query.requesterRole())) {
            throw new UnauthorizedActionException("Only students or admins can access attempts");
        }

        Solution solution = solutionRepository.findById(query.solutionId())
            .orElseThrow(SolutionNotFoundException::new);

        if (!UserRole.ROLE_ADMIN.equals(query.requesterRole()) && !solution.isOwnedBy(query.requesterId())) {
            throw new SolutionOwnershipException("Only the solution owner or admin can access its attempts.");
        }

        return attemptRepository.findAllBySolutionIdOrderByCreatedAtDesc(query.solutionId());
    }
}
