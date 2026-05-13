package com.codexp.solutions.solutions.domain.model;

import com.codexp.solutions.shared.domain.model.AbstractEntity;
import com.codexp.solutions.solutions.domain.exceptions.AttemptsLimitReachedException;
import com.codexp.solutions.solutions.domain.model.valueobjects.*;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
    name = "solutions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_solution_challenge_author_language",
            columnNames = { "challenge_id", "author_id", "language" }
        ),
    }
)
@NoArgsConstructor
public class Solution extends AbstractEntity {

    @EmbeddedId
    @AttributeOverride(
        name = "value",
        column = @Column(
            name = "solution_id",
            nullable = false,
            updatable = false
        )
    )
    private SolutionId id;

    @Embedded
    @AttributeOverride(
        name = "value",
        column = @Column(name = "challenge_id", nullable = false)
    )
    private ChallengeId challengeId;

    @Embedded
    @AttributeOverride(
        name = "value",
        column = @Column(name = "code_template_id", nullable = false)
    )
    private CodeTemplateId codeTemplateId;

    @Embedded
    @AttributeOverride(
        name = "value",
        column = @Column(name = "author_id", nullable = false)
    )
    private AuthorId authorId;

    @Embedded
    @AttributeOverride(
        name = "value",
        column = @Column(name = "language", nullable = false, length = 30)
    )
    private TemplateLanguage language;

    @Embedded
    @AttributeOverride(
        name = "value",
        column = @Column(
            name = "code",
            nullable = false,
            columnDefinition = "TEXT"
        )
    )
    private SolutionCode code;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SolutionStatus status;

    @Embedded
    @AttributeOverride(
        name = "value",
        column = @Column(name = "max_attempts", nullable = false)
    )
    private AttemptsLimit maxAttempts;

    @Embedded
    @AttributeOverride(
        name = "value",
        column = @Column(name = "current_attempts", nullable = false)
    )
    private AttemptsCount currentAttempts;

    @Column(name = "attempts_reset_at")
    private Instant attemptsResetAt;

    public static Solution create(
        SolutionId id,
        ChallengeId challengeId,
        com.codexp.solutions.solutions.domain.model.valueobjects.CodeTemplateId codeTemplateId,
        AuthorId authorId,
        TemplateLanguage language,
        SolutionCode code,
        AttemptsLimit maxAttempts
    ) {
        Solution solution = new Solution();
        solution.id = id;
        solution.challengeId = challengeId;
        solution.codeTemplateId = codeTemplateId;
        solution.authorId = authorId;
        solution.language = language;
        solution.code = code;
        solution.status = SolutionStatus.DRAFT;
        solution.maxAttempts = maxAttempts;
        solution.currentAttempts = AttemptsCount.zero();
        solution.attemptsResetAt = null;
        return solution;
    }

    public void updateCode(SolutionCode code) {
        this.code = code;
    }

    public boolean isOwnedBy(AuthorId author) {
        return this.authorId.equals(author);
    }

    public int remainingAttempts() {
        return Math.max(0, maxAttempts.value() - currentAttempts.value());
    }

    public boolean canSubmit(Instant now) {
        if (currentAttempts.value() < maxAttempts.value()) {
            return true;
        }

        return attemptsResetAt != null && now.isAfter(attemptsResetAt);
    }

    public void submit(Instant now, AttemptWindowMinutes resetWindow) {
        if (!canSubmit(now)) {
            throw new AttemptsLimitReachedException(
                "Attempts limit reached. You can submit again after " +
                    attemptsResetAt
            );
        }

        if (attemptsResetAt != null && now.isAfter(attemptsResetAt)) {
            currentAttempts = AttemptsCount.zero();
            attemptsResetAt = null;
        }

        currentAttempts = currentAttempts.increment();
        if (currentAttempts.value() >= maxAttempts.value()) {
            attemptsResetAt = now.plusSeconds(resetWindow.value() * 60L);
        }

        status = SolutionStatus.QUEUED;
    }

    public void markExecuting() {
        status = SolutionStatus.EXECUTING;
    }

    public void markCompleted(boolean isSuccessful) {
        status = isSuccessful ? SolutionStatus.PASSED : SolutionStatus.FAILED;
    }
}
