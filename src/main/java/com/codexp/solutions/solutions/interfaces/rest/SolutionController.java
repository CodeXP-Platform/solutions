package com.codexp.solutions.solutions.interfaces.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codexp.solutions.shared.application.UserContext;
import com.codexp.solutions.solutions.domain.model.queries.GetLatestAttemptBySolutionIdQuery;
import com.codexp.solutions.solutions.domain.services.AttemptQueryService;
import com.codexp.solutions.solutions.domain.services.SolutionCommandService;
import com.codexp.solutions.solutions.domain.services.SolutionQueryService;
import com.codexp.solutions.solutions.interfaces.rest.requests.CreateSolutionRequest;
import com.codexp.solutions.solutions.interfaces.rest.requests.UpdateSolutionCodeRequest;
import com.codexp.solutions.solutions.interfaces.rest.responses.SolutionResponse;
import com.codexp.solutions.solutions.interfaces.rest.responses.SubmitSolutionResponse;
import com.codexp.solutions.solutions.interfaces.rest.transformers.SolutionAssembler;
import com.codexp.solutions.solutions.interfaces.rest.transformers.SolutionCommandAssembler;
import com.codexp.solutions.solutions.interfaces.rest.transformers.SolutionChallengeQueryAssembler;
import com.codexp.solutions.solutions.interfaces.rest.transformers.SolutionCreateCommandAssembler;
import com.codexp.solutions.solutions.interfaces.rest.transformers.SolutionQueryAssembler;

@RestController
@RequestMapping("/api/v1/solutions")
public class SolutionController {

    private final SolutionCommandService solutionCommandService;
    private final SolutionQueryService solutionQueryService;
    private final AttemptQueryService attemptQueryService;
    private final UserContext userContext;

    public SolutionController(
        SolutionCommandService solutionCommandService,
        SolutionQueryService solutionQueryService,
        AttemptQueryService attemptQueryService,
        UserContext userContext
    ) {
        this.solutionCommandService = solutionCommandService;
        this.solutionQueryService = solutionQueryService;
        this.attemptQueryService = attemptQueryService;
        this.userContext = userContext;
    }

    @PostMapping
    public ResponseEntity<SolutionResponse> create(
        @RequestBody CreateSolutionRequest request
    ) {
        var jwt = userContext.getPrincipal();

        var command = SolutionCreateCommandAssembler.toCreateSolutionCommand(
            request,
            jwt.userId().value(),
            jwt.role()
        );
        solutionCommandService.handle(command);

        var query = SolutionChallengeQueryAssembler.toGetSolutionByChallengeQuery(
            request.challengeId(),
            jwt.userId().value(),
            jwt.role(),
            request.language()
        );
        var solution = solutionQueryService.handle(query);
        var latestAttempt = attemptQueryService
            .handle(new GetLatestAttemptBySolutionIdQuery(solution.getId()))
            .orElse(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(SolutionAssembler.toResponse(solution, latestAttempt));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolutionResponse> getById(@PathVariable String id) {
        var jwt = userContext.getPrincipal();

        var query = SolutionQueryAssembler.toGetSolutionByIdQuery(
            id,
            jwt.userId().value(),
            jwt.role()
        );
        var solution = solutionQueryService.handle(query);
        var latestAttempt = attemptQueryService
            .handle(new GetLatestAttemptBySolutionIdQuery(solution.getId()))
            .orElse(null);

        return ResponseEntity.ok(SolutionAssembler.toResponse(solution, latestAttempt));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolutionResponse> updateCode(
        @PathVariable String id,
        @RequestBody UpdateSolutionCodeRequest request
    ) {
        var jwt = userContext.getPrincipal();

        var command = SolutionCommandAssembler.toUpdateSolutionCodeCommand(
            id,
            request,
            jwt.userId().value(),
            jwt.role()
        );

        var solution = solutionCommandService.handle(command);
        var latestAttempt = attemptQueryService
            .handle(new GetLatestAttemptBySolutionIdQuery(solution.getId()))
            .orElse(null);

        return ResponseEntity.ok(SolutionAssembler.toResponse(solution, latestAttempt));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<SubmitSolutionResponse> submit(@PathVariable String id) {
        var jwt = userContext.getPrincipal();

        var command = SolutionCommandAssembler.toSubmitSolutionCommand(
            id,
            jwt.userId().value(),
            jwt.role()
        );

        var solution = solutionCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(SolutionAssembler.toSubmitResponse(solution));
    }
}
