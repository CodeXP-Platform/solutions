package com.codexp.solutions.solutions.interfaces.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.codexp.solutions.solutions.domain.model.commands.CreateSolutionFromRequestedEventCommand;
import com.codexp.solutions.solutions.domain.model.events.SolutionRequestedEvent;
import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.ChallengeId;
import com.codexp.solutions.solutions.domain.model.valueobjects.SolutionCode;
import com.codexp.solutions.solutions.domain.model.valueobjects.TemplateLanguage;
import com.codexp.solutions.solutions.domain.services.SolutionCommandService;

@Component
public class SolutionRequestedEventsListener {

    private final SolutionCommandService solutionCommandService;

    public SolutionRequestedEventsListener(SolutionCommandService solutionCommandService) {
        this.solutionCommandService = solutionCommandService;
    }

    @RabbitListener(queues = "${app.messaging.solutions.queues.solution-requested}")
    public void onSolutionRequested(SolutionRequestedEvent event) {
        var data = event.data();

        var command = new CreateSolutionFromRequestedEventCommand(
            ChallengeId.fromString(data.challengeId()),
            AuthorId.fromString(data.authorId()),
            TemplateLanguage.fromString(data.language()),
            SolutionCode.fromString(data.templateCode())
        );

        solutionCommandService.handle(command);
    }
}
