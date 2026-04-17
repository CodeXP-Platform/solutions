package com.codexp.solutions.shared.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMq {

    @Bean
    public TopicExchange challengesExchange(
            @Value("${app.messaging.challenges.exchange}") String exchangeName
    ) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public TopicExchange codeExecutionExchange(
            @Value("${app.messaging.code-execution.exchange}") String exchangeName
    ) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public Queue solutionExecutionStartedQueue(
            @Value("${app.messaging.solutions.queues.execution-started}") String queueName
    ) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Queue solutionExecutionCompletedQueue(
            @Value("${app.messaging.solutions.queues.execution-completed}") String queueName
    ) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Queue solutionRequestedQueue(
            @Value("${app.messaging.solutions.queues.solution-requested}") String queueName
    ) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding solutionExecutionStartedBinding(
            Queue solutionExecutionStartedQueue,
            TopicExchange codeExecutionExchange,
            @Value("${app.messaging.solutions.routing-keys.execution-started}") String routingKey
    ) {
        return BindingBuilder.bind(solutionExecutionStartedQueue).to(codeExecutionExchange).with(routingKey);
    }

    @Bean
    public Binding solutionExecutionCompletedBinding(
            Queue solutionExecutionCompletedQueue,
            TopicExchange codeExecutionExchange,
            @Value("${app.messaging.solutions.routing-keys.execution-completed}") String routingKey
    ) {
        return BindingBuilder.bind(solutionExecutionCompletedQueue).to(codeExecutionExchange).with(routingKey);
    }

    @Bean
    public Binding solutionRequestedBinding(
            Queue solutionRequestedQueue,
            TopicExchange challengesExchange,
            @Value("${app.messaging.solutions.routing-keys.solution-requested}") String routingKey
    ) {
        return BindingBuilder.bind(solutionRequestedQueue).to(challengesExchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
