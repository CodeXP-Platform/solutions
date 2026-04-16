package com.codexp.solutions.solutions.infrastructure.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.codexp.solutions.solutions.domain.model.events.SolutionExecutionRequestedEvent;
import com.codexp.solutions.solutions.domain.services.SolutionExecutionEventPublisher;

@Component
public class SolutionExecutionEventPublisherImpl implements SolutionExecutionEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String challengesExchange;
    private final String executionRequestedRoutingKey;

    public SolutionExecutionEventPublisherImpl(
        RabbitTemplate rabbitTemplate,
        @Value("${app.messaging.challenges.exchange}") String challengesExchange,
        @Value("${app.messaging.solutions.routing-keys.execution-requested}") String executionRequestedRoutingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.challengesExchange = challengesExchange;
        this.executionRequestedRoutingKey = executionRequestedRoutingKey;
    }

    @Override
    public void publish(SolutionExecutionRequestedEvent event) {
        rabbitTemplate.convertAndSend(challengesExchange, executionRequestedRoutingKey, event);
    }
}
