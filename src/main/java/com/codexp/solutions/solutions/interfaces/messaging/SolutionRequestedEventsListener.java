package com.codexp.solutions.solutions.interfaces.messaging;

import com.codexp.solutions.shared.domain.model.valueobjects.UserRole;
import com.codexp.solutions.solutions.domain.model.commands.CreateSolutionCommand;
import com.codexp.solutions.solutions.domain.model.events.SolutionRequestedEvent;
import com.codexp.solutions.solutions.domain.model.valueobjects.AuthorId;
import com.codexp.solutions.solutions.domain.model.valueobjects.ChallengeId;
import com.codexp.solutions.solutions.domain.model.valueobjects.CodeTemplateId;
import com.codexp.solutions.solutions.domain.model.valueobjects.TemplateLanguage;
import com.codexp.solutions.solutions.domain.services.SolutionCommandService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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
            CodeTemplateId.fromString(data.codeTemplateId()),
            AuthorId.fromString(data.authorId()),
            UserRole.ROLE_STUDENT,
            TemplateLanguage.fromString(data.language())
        );

        solutionCommandService.handle(command);
    }
}
