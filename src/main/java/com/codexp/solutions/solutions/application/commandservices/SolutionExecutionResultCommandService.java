package com.codexp.solutions.solutions.application.commandservices;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codexp.solutions.solutions.domain.exceptions.SolutionNotFoundException;
import com.codexp.solutions.solutions.domain.model.events.SolutionExecutionCompletedEvent;
import com.codexp.solutions.solutions.domain.model.events.SolutionExecutionStartedEvent;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionId;
import com.codexp.solutions.solutions.infrastructure.persistence.jpa.repositories.SolutionRepository;

@Service
public class SolutionExecutionResultCommandService {

    private final SolutionRepository solutionRepository;

    public SolutionExecutionResultCommandService(SolutionRepository solutionRepository) {
        this.solutionRepository = solutionRepository;
    }

    @Transactional
    public void handle(SolutionExecutionStartedEvent event) {
        var solutionId = SolutionId.fromString(event.data().solutionId());
        var solution = solutionRepository.findById(solutionId).orElseThrow(SolutionNotFoundException::new);
        solution.markExecuting();
        solutionRepository.save(solution);
    }

    @Transactional
    public void handle(SolutionExecutionCompletedEvent event) {
        var solutionId = SolutionId.fromString(event.data().solutionId());
        var solution = solutionRepository.findById(solutionId).orElseThrow(SolutionNotFoundException::new);

        var failedTestIds = new ArrayList<String>();
        if (event.data().testResults() != null) {
            event.data().testResults().stream()
                .filter(result -> Boolean.FALSE.equals(result.passed()))
                .map(SolutionExecutionCompletedEvent.TestResult::testId)
                .forEach(failedTestIds::add);
        }

        solution.markCompleted(
            Boolean.TRUE.equals(event.data().isSuccessful()),
            event.data().totalExecutionTimeMs(),
            event.data().globalError(),
            failedTestIds
        );

        solutionRepository.save(solution);
    }
}
