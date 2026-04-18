package com.codexp.solutions.solutions.interfaces.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import com.codexp.solutions.solutions.domain.model.commands.CreateSolutionCommand;
import com.codexp.solutions.solutions.domain.model.events.SolutionRequestedEvent;
import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.ChallengeId;
import com.codexp.solutions.solutions.domain.model.valueobjects.TemplateLanguage;
import com.codexp.solutions.solutions.domain.services.SolutionCommandService;

@Component
public class SolutionRequestedEventsListener {

    private final SolutionCommandService solutionCommandService;

    public SolutionRequestedEventsListener(
        SolutionCommandService solutionCommandService
    ) {
        this.solutionCommandService = solutionCommandService;
    }

    @RabbitListener(
        queues = "${app.messaging.solutions.queues.solution-requested}"
    )
    public void onSolutionRequested(SolutionRequestedEvent event) {
        var data = event.data();

        var command = new CreateSolutionCommand(
            ChallengeId.fromString(data.challengeId()),
            AuthorId.fromString(data.authorId()),
            UserRole.ROLE_STUDENT,
            TemplateLanguage.fromString(data.language())
        );

        solutionCommandService.handle(command);
    }
}
