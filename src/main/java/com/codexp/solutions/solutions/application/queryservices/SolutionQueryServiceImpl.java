package com.codexp.solutions.solutions.application.queryservices;

import org.springframework.stereotype.Service;

import com.codexp.solutions.shared.domain.exceptions.UnauthorizedActionException;
import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import com.codexp.solutions.solutions.domain.exceptions.SolutionNotFoundException;
import com.codexp.solutions.solutions.domain.exceptions.SolutionOwnershipException;
import com.codexp.solutions.solutions.domain.model.Solution;
import com.codexp.solutions.solutions.domain.model.queries.GetSolutionByChallengeQuery;
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
        ensureAllowedRole(query.requesterRole());

        Solution solution = solutionRepository.findById(query.solutionId()).orElseThrow(SolutionNotFoundException::new);

        if (!UserRole.ROLE_ADMIN.equals(query.requesterRole()) && !solution.isOwnedBy(query.requesterId())) {
            throw new SolutionOwnershipException("Only the solution owner or admin can access this solution.");
        }

        return solution;
    }

    @Override
    public Solution handle(GetSolutionByChallengeQuery query) {
        ensureAllowedRole(query.requesterRole());

        return solutionRepository
            .findByChallengeIdAndAuthorIdAndLanguage(
                query.challengeId(),
                query.requesterId(),
                query.language()
            )
            .orElseThrow(SolutionNotFoundException::new);
    }

    private void ensureAllowedRole(UserRole role) {
        if (!UserRole.ROLE_STUDENT.equals(role) && !UserRole.ROLE_ADMIN.equals(role)) {
            throw new UnauthorizedActionException("Only students or admins can access solutions");
        }
    }
}
