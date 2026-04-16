package com.codexp.solutions.solutions.infrastructure.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codexp.solutions.solutions.domain.model.Attempt;
import com.codexp.solutions.solutions.domain.model.valueobjects.AttemptId;
import com.codexp.solutions.solutions.domain.model.valueobjects.AttemptStatus;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionId;

public interface AttemptRepository extends JpaRepository<Attempt, AttemptId> {
    Optional<Attempt> findTopBySolutionIdOrderByCreatedAtDesc(SolutionId solutionId);

    Optional<Attempt> findFirstBySolutionIdAndStatusOrderByCreatedAtAsc(
        SolutionId solutionId,
        AttemptStatus status
    );

    Optional<Attempt> findTopBySolutionIdAndStatusOrderByCreatedAtDesc(
        SolutionId solutionId,
        AttemptStatus status
    );
}
