package com.codexp.solutions.solutions.application.eventhandlers;

import java.util.ArrayList;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.codexp.solutions.solutions.domain.exceptions.SolutionNotFoundException;
import com.codexp.solutions.solutions.domain.model.valueobjects.AttemptStatus;
import com.codexp.solutions.solutions.domain.model.events.SolutionExecutionCompletedEvent;
import com.codexp.solutions.solutions.domain.model.events.SolutionExecutionStartedEvent;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionId;
import com.codexp.solutions.solutions.infrastructure.persistence.jpa.repositories.AttemptRepository;
import com.codexp.solutions.solutions.infrastructure.persistence.jpa.repositories.SolutionRepository;

@Component
public class SolutionExecutionResultEventHandler {

    private final SolutionRepository solutionRepository;
    private final AttemptRepository attemptRepository;

    public SolutionExecutionResultEventHandler(
        SolutionRepository solutionRepository,
        AttemptRepository attemptRepository
    ) {
        this.solutionRepository = solutionRepository;
        this.attemptRepository = attemptRepository;
    }

    @Transactional
    public void handleExecutionStarted(SolutionExecutionStartedEvent event) {
        var solutionId = SolutionId.fromString(event.data().solutionId());
        var solution = solutionRepository.findById(solutionId).orElseThrow(SolutionNotFoundException::new);

        if (event.data().executionId() == null || event.data().executionId().isBlank()) {
            throw new IllegalArgumentException("Execution started event must include executionId");
        }

        var attempt = attemptRepository
            .findFirstBySolutionIdAndStatusOrderByCreatedAtAsc(solutionId, AttemptStatus.QUEUED)
            .or(() -> attemptRepository.findFirstBySolutionIdAndStatusOrderByCreatedAtAsc(solutionId, AttemptStatus.EXECUTING))
            .orElseThrow(() -> new IllegalArgumentException("No queued attempt found for started execution event."));

        if (AttemptStatus.EXECUTING.equals(attempt.getStatus())) {
            return;
        }

        attempt.markExecuting();
        solution.markExecuting();

        attemptRepository.save(attempt);
        solutionRepository.save(solution);
    }

    @Transactional
    public void handleExecutionCompleted(SolutionExecutionCompletedEvent event) {
        var solutionId = SolutionId.fromString(event.data().solutionId());
        var solution = solutionRepository.findById(solutionId).orElseThrow(SolutionNotFoundException::new);

        if (event.data().executionId() == null || event.data().executionId().isBlank()) {
            throw new IllegalArgumentException("Execution completed event must include executionId");
        }

        var attempt = attemptRepository
            .findFirstBySolutionIdAndStatusOrderByCreatedAtAsc(solutionId, AttemptStatus.EXECUTING)
            .or(() -> attemptRepository.findFirstBySolutionIdAndStatusOrderByCreatedAtAsc(solutionId, AttemptStatus.QUEUED))
            .or(() -> attemptRepository.findTopBySolutionIdAndStatusOrderByCreatedAtDesc(solutionId, AttemptStatus.PASSED))
            .or(() -> attemptRepository.findTopBySolutionIdAndStatusOrderByCreatedAtDesc(solutionId, AttemptStatus.FAILED))
            .orElseThrow(() -> new IllegalArgumentException("No in-flight attempt found for completed execution event."));

        if (AttemptStatus.PASSED.equals(attempt.getStatus()) || AttemptStatus.FAILED.equals(attempt.getStatus())) {
            return;
        }

        var failedTestIds = new ArrayList<String>();
        if (event.data().testResults() != null) {
            event.data().testResults().stream()
                .filter(result -> Boolean.FALSE.equals(result.passed()))
                .map(SolutionExecutionCompletedEvent.TestResult::testId)
                .forEach(failedTestIds::add);
        }

        boolean isSuccessful = Boolean.TRUE.equals(event.data().isSuccessful());
        attempt.markCompleted(
            isSuccessful,
            event.data().totalExecutionTimeMs() == null
                ? null
                : event.data().totalExecutionTimeMs().longValue(),
            event.data().globalError(),
            failedTestIds
        );
        solution.markCompleted(isSuccessful);

        attemptRepository.save(attempt);
        solutionRepository.save(solution);
    }
}
