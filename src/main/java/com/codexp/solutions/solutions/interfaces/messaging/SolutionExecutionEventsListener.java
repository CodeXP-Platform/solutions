package com.codexp.solutions.solutions.interfaces.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.codexp.solutions.solutions.application.eventhandlers.SolutionExecutionResultEventHandler;
import com.codexp.solutions.solutions.domain.model.events.SolutionExecutionCompletedEvent;
import com.codexp.solutions.solutions.domain.model.events.SolutionExecutionStartedEvent;

@Component
public class SolutionExecutionEventsListener {

    private final SolutionExecutionResultEventHandler solutionExecutionResultEventHandler;

    public SolutionExecutionEventsListener(SolutionExecutionResultEventHandler solutionExecutionResultEventHandler) {
        this.solutionExecutionResultEventHandler = solutionExecutionResultEventHandler;
    }

    @RabbitListener(queues = "${app.messaging.solutions.queues.execution-started}")
    public void onExecutionStarted(SolutionExecutionStartedEvent event) {
        solutionExecutionResultEventHandler.handleExecutionStarted(event);
    }

    @RabbitListener(queues = "${app.messaging.solutions.queues.execution-completed}")
    public void onExecutionCompleted(SolutionExecutionCompletedEvent event) {
        solutionExecutionResultEventHandler.handleExecutionCompleted(event);
    }
}
