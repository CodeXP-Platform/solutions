package com.codexp.solutions.solutions.domain.model;

import com.codexp.solutions.shared.domain.model.AbstractEntity;
import com.codexp.solutions.solutions.domain.exceptions.AttemptsLimitReachedException;
import com.codexp.solutions.solutions.domain.model.valueobjects.AttemptWindowMinutes;
import com.codexp.solutions.solutions.domain.model.valueobjects.AttemptsCount;
import com.codexp.solutions.solutions.domain.model.valueobjects.AttemptsLimit;
import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.ChallengeId;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionCode;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionId;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionStatus;
import com.codexp.solutions.solutions.domain.model.valueobjects.TemplateLanguage;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
    name = "solutions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_solution_challenge_author",
            columnNames = { "challenge_id", "author_id" }
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

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    @Column(name = "error_details", columnDefinition = "TEXT")
    private String errorDetails;

    @ElementCollection
    @CollectionTable(
        name = "solution_failed_tests",
        joinColumns = @JoinColumn(name = "solution_id")
    )
    @Column(name = "test_id", nullable = false)
    private List<String> failedTestIds = new ArrayList<>();

    public static Solution create(
        SolutionId id,
        ChallengeId challengeId,
        AuthorId authorId,
        TemplateLanguage language,
        SolutionCode code,
        AttemptsLimit maxAttempts
    ) {
        Solution solution = new Solution();
        solution.id = id;
        solution.challengeId = challengeId;
        solution.authorId = authorId;
        solution.language = language;
        solution.code = code;
        solution.status = SolutionStatus.DRAFT;
        solution.maxAttempts = maxAttempts;
        solution.currentAttempts = AttemptsCount.zero();
        solution.attemptsResetAt = null;
        solution.executionTimeMs = null;
        solution.errorDetails = null;
        solution.failedTestIds = new ArrayList<>();
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
        errorDetails = null;
        failedTestIds.clear();
        executionTimeMs = null;
    }

    public void markExecuting() {
        status = SolutionStatus.EXECUTING;
    }

    public void markCompleted(
        boolean isSuccessful,
        Long totalExecutionTimeMs,
        String globalError,
        List<String> failedTestIds
    ) {
        status = isSuccessful ? SolutionStatus.PASSED : SolutionStatus.FAILED;
        executionTimeMs = totalExecutionTimeMs;
        errorDetails = globalError;
        this.failedTestIds =
            failedTestIds == null
                ? new ArrayList<>()
                : failedTestIds
                      .stream()
                      .distinct()
                      .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<String> failedTestIdsView() {
        return Collections.unmodifiableList(failedTestIds);
    }
}
