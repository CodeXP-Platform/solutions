package com.codexp.solutions.solutions.domain.services;

import com.codexp.solutions.solutions.domain.model.events.SolutionExecutionRequestedEvent;

public interface SolutionExecutionEventPublisher {
    void publish(SolutionExecutionRequestedEvent event);
}
