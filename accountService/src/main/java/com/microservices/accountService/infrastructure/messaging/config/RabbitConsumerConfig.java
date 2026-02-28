package com.microservices.accountService.infrastructure.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitConsumerConfig {
    public static final String CUSTOMER_EXCHANGE = "customer.events";
    public static final String CUSTOMER_QUEUE = "account.customer.events.q";
    public static final String BINDING_PATTERN = "customer.*";

    @Bean
    TopicExchange customerExchange() {
        return new TopicExchange(CUSTOMER_EXCHANGE, true, true);
    }

    @Bean
    Queue customerQueue() {
        return QueueBuilder.durable(CUSTOMER_QUEUE).build();
    }

    @Bean
    Binding customerEventsBinding(Queue customerEventsQueue, TopicExchange customerEventsExchange) {
        return BindingBuilder.bind(customerEventsQueue).to(customerEventsExchange).with(BINDING_PATTERN);
    }

    @Bean
    JsonMapper jsonMapper() {
        return JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    @Bean
    MessageConverter messageConverter(JsonMapper jsonMapper) {
        return new JacksonJsonMessageConverter(jsonMapper, "com.microservices");
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
