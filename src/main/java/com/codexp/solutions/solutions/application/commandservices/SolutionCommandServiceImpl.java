package com.codexp.solutions.solutions.application.commandservices;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codexp.solutions.shared.domain.exceptions.UnauthorizedActionException;
import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import com.codexp.solutions.solutions.domain.exceptions.ChallengeContextFetchException;
import com.codexp.solutions.solutions.domain.exceptions.SolutionNotFoundException;
import com.codexp.solutions.solutions.domain.exceptions.SolutionOwnershipException;
import com.codexp.solutions.solutions.domain.model.commands.CreateSolutionFromRequestedEventCommand;
import com.codexp.solutions.solutions.domain.model.commands.SubmitSolutionCommand;
import com.codexp.solutions.solutions.domain.model.commands.UpdateSolutionCodeCommand;
import com.codexp.solutions.solutions.domain.model.events.SolutionExecutionRequestedEvent;
import com.codexp.solutions.solutions.domain.model.valueobjects.AttemptWindowMinutes;
import com.codexp.solutions.solutions.domain.model.valueobjects.AttemptsLimit;
import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionId;
import com.codexp.solutions.solutions.domain.services.ChallengeContextGateway;
import com.codexp.solutions.solutions.domain.services.SolutionCommandService;
import com.codexp.solutions.solutions.domain.services.SolutionExecutionEventPublisher;
import com.codexp.solutions.solutions.infrastructure.persistence.jpa.repositories.SolutionRepository;

import com.codexp.solutions.solutions.domain.model.Solution;

@Service
public class SolutionCommandServiceImpl implements SolutionCommandService {

    private final SolutionRepository solutionRepository;
    private final ChallengeContextGateway challengeContextGateway;
    private final SolutionExecutionEventPublisher solutionExecutionEventPublisher;

    @Value("${app.solutions.default-max-attempts}")
    private int defaultMaxAttempts;

    @Value("${app.solutions.attempt-reset-window-minutes}")
    private int attemptResetWindowMinutes;

    public SolutionCommandServiceImpl(
        SolutionRepository solutionRepository,
        ChallengeContextGateway challengeContextGateway,
        SolutionExecutionEventPublisher solutionExecutionEventPublisher
    ) {
        this.solutionRepository = solutionRepository;
        this.challengeContextGateway = challengeContextGateway;
        this.solutionExecutionEventPublisher = solutionExecutionEventPublisher;
    }

    @Override
    @Transactional
    public Solution handle(CreateSolutionFromRequestedEventCommand command) {
        var existing = solutionRepository.findByChallengeIdAndAuthorId(command.challengeId(), command.authorId());
        if (existing.isPresent()) {
            return existing.get();
        }

        Solution solution = Solution.create(
            SolutionId.generate(),
            command.challengeId(),
            command.authorId(),
            command.language(),
            command.templateCode(),
            AttemptsLimit.fromInt(defaultMaxAttempts)
        );

        return solutionRepository.save(solution);
    }

    @Override
    @Transactional
    public Solution handle(UpdateSolutionCodeCommand command) {
        ensureStudent(command.requesterRole());

        Solution solution = solutionRepository.findById(command.solutionId()).orElseThrow(SolutionNotFoundException::new);
        assertOwnership(solution, command.requesterId());

        solution.updateCode(command.code());
        return solutionRepository.save(solution);
    }

    @Override
    @Transactional
    public Solution handle(SubmitSolutionCommand command) {
        ensureStudent(command.requesterRole());

        Solution solution = solutionRepository.findById(command.solutionId()).orElseThrow(SolutionNotFoundException::new);
        assertOwnership(solution, command.requesterId());

        var submitContext = challengeContextGateway.fetchSubmitContext(solution.getChallengeId(), solution.getLanguage());
        if (submitContext == null || submitContext.testCases() == null || submitContext.testCases().isEmpty()) {
            throw new ChallengeContextFetchException("Challenge submit context did not include test cases.");
        }

        if (submitContext.entryFunctionName() == null || submitContext.entryFunctionName().isBlank()) {
            throw new ChallengeContextFetchException("Challenge submit context did not include a valid entry function name.");
        }

        submitContext.testCases().forEach(testCase -> validateTestInput(testCase.input()));

        solution.submit(Instant.now(), AttemptWindowMinutes.fromInt(attemptResetWindowMinutes));
        Solution saved = solutionRepository.save(solution);

        var eventTestCases = new ArrayList<SolutionExecutionRequestedEvent.TestCaseData>();
        for (var testCase : submitContext.testCases()) {
            if (testCase.testId() == null || testCase.testId().isBlank()) {
                throw new ChallengeContextFetchException("Challenge submit context contains a test case without testId.");
            }

            if (testCase.expectedOutput() == null) {
                throw new ChallengeContextFetchException("Challenge submit context contains a test case without expectedOutput.");
            }

            eventTestCases.add(
                new SolutionExecutionRequestedEvent.TestCaseData(
                    testCase.testId(),
                    testCase.input(),
                    testCase.expectedOutput(),
                    Objects.requireNonNullElse(testCase.isHidden(), Boolean.FALSE)
                )
            );
        }

        SolutionExecutionRequestedEvent event = SolutionExecutionRequestedEvent.create(
            saved.getId().toString(),
            saved.getLanguage().toString(),
            submitContext.entryFunctionName(),
            saved.getCode().toString(),
            eventTestCases
        );

        solutionExecutionEventPublisher.publish(event);

        return saved;
    }

    private void validateTestInput(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Test case input cannot be blank.");
        }

        if (input.contains("\n") || input.contains("\r")) {
            throw new IllegalArgumentException("Test case input must use comma as primary argument separator.");
        }

        if (input.contains("\\\\")) {
            throw new IllegalArgumentException("Test case input cannot use escaped backslashes to segment arguments.");
        }
    }

    private void ensureStudent(UserRole role) {
        if (!UserRole.ROLE_STUDENT.equals(role)) {
            throw new UnauthorizedActionException("Only students can manage solutions");
        }
    }

    private void assertOwnership(Solution solution, AuthorId requesterId) {
        if (!solution.isOwnedBy(requesterId)) {
            throw new SolutionOwnershipException("Only the solution owner can modify or submit this solution.");
        }
    }
}
