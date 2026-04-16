package com.codexp.solutions.solutions.interfaces.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.codexp.solutions.solutions.application.commandservices.SolutionExecutionResultCommandService;
import com.codexp.solutions.solutions.domain.model.events.SolutionExecutionCompletedEvent;
import com.codexp.solutions.solutions.domain.model.events.SolutionExecutionStartedEvent;

@Component
public class SolutionExecutionEventsListener {

    private final SolutionExecutionResultCommandService solutionExecutionResultCommandService;

    public SolutionExecutionEventsListener(SolutionExecutionResultCommandService solutionExecutionResultCommandService) {
        this.solutionExecutionResultCommandService = solutionExecutionResultCommandService;
    }

    @RabbitListener(queues = "${app.messaging.solutions.queues.execution-started}")
    public void onExecutionStarted(SolutionExecutionStartedEvent event) {
        solutionExecutionResultCommandService.handle(event);
    }

    @RabbitListener(queues = "${app.messaging.solutions.queues.execution-completed}")
    public void onExecutionCompleted(SolutionExecutionCompletedEvent event) {
        solutionExecutionResultCommandService.handle(event);
    }
}
