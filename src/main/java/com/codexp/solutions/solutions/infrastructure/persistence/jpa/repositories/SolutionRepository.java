package com.codexp.solutions.solutions.infrastructure.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codexp.solutions.solutions.domain.model.Solution;
import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.ChallengeId;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionId;
import com.codexp.solutions.solutions.domain.model.valueobjects.TemplateLanguage;

public interface SolutionRepository extends JpaRepository<Solution, SolutionId> {
    Optional<Solution> findByChallengeIdAndAuthorIdAndLanguage(
        ChallengeId challengeId,
        AuthorId authorId,
        TemplateLanguage language
    );
}
