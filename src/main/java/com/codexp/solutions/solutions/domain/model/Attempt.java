package com.codexp.solutions.solutions.domain.model;

import com.codexp.solutions.shared.domain.model.AbstractEntity;
import com.codexp.solutions.solutions.domain.model.valueobjects.AttemptId;
import com.codexp.solutions.solutions.domain.model.valueobjects.AttemptStatus;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionId;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attempts")
@Getter
@NoArgsConstructor
public class Attempt extends AbstractEntity {

    @EmbeddedId
    @AttributeOverride(
        name = "value",
        column = @Column(
            name = "attempt_id",
            nullable = false,
            updatable = false
        )
    )
    private AttemptId id;

    @Embedded
    @AttributeOverride(
        name = "value",
        column = @Column(name = "solution_id", nullable = false)
    )
    private SolutionId solutionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AttemptStatus status;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    @Column(name = "error_details", columnDefinition = "TEXT")
    private String errorDetails;

    @ElementCollection
    @CollectionTable(
        name = "attempt_failed_tests",
        joinColumns = @JoinColumn(name = "attempt_id")
    )
    @Column(name = "test_id", nullable = false)
    private List<String> failedTestIds = new ArrayList<>();

    public static Attempt createQueued(AttemptId id, SolutionId solutionId) {
        Attempt attempt = new Attempt();
        attempt.id = id;
        attempt.solutionId = solutionId;
        attempt.status = AttemptStatus.QUEUED;
        attempt.executionTimeMs = null;
        attempt.errorDetails = null;
        attempt.failedTestIds = new ArrayList<>();
        return attempt;
    }

    public void markExecuting() {
        status = AttemptStatus.EXECUTING;
    }

    public void markCompleted(
        boolean isSuccessful,
        Long totalExecutionTimeMs,
        String globalError,
        List<String> failedTestIds
    ) {
        status = isSuccessful ? AttemptStatus.PASSED : AttemptStatus.FAILED;
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
