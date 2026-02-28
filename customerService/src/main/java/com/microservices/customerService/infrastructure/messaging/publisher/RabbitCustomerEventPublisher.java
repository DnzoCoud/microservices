package com.microservices.customerService.infrastructure.messaging.publisher;

import com.microservices.customerService.domain.model.Customer;
import com.microservices.customerService.domain.model.CustomerEventTypes;
import com.microservices.customerService.domain.model.CustomerId;
import com.microservices.customerService.domain.port.CustomerEventPublisherPort;
import com.microservices.customerService.infrastructure.messaging.config.RabbitMessagingConfig;
import com.microservices.customerService.infrastructure.messaging.dto.CustomerEventData;
import com.microservices.customerService.infrastructure.messaging.dto.CustomerEventEnvelope;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class RabbitCustomerEventPublisher implements CustomerEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public RabbitCustomerEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishCreated(Customer customer) {
        send(CustomerEventTypes.CUSTOMER_CREATED.getType(), customer);
    }

    @Override
    public void publishUpdated(Customer customer) {
        send(CustomerEventTypes.CUSTOMER_UPDATED.getType(), customer);
    }

    @Override
    public void publishDeleted(CustomerId customerId) {
        var envelope = new CustomerEventEnvelope(
            UUID.randomUUID().toString(),
            CustomerEventTypes.CUSTOMER_DELETED.getType(),
            1,
            Instant.now(),
            new CustomerEventData(customerId.getValue(), "deleted", false)
        );

        rabbitTemplate.convertAndSend(RabbitMessagingConfig.CUSTOMER_EXCHANGE, CustomerEventTypes.CUSTOMER_DELETED.getType(), envelope);
    }

    private void send(String type, Customer customer) {
        var data = new CustomerEventData(
            customer.getCustomerId().getValue(),
            customer.getName(),
            customer.isActive()
        );

        var envelope = new CustomerEventEnvelope(
            UUID.randomUUID().toString(),
            type,
            1,
            Instant.now(),
            data
        );
        rabbitTemplate.convertAndSend(RabbitMessagingConfig.CUSTOMER_EXCHANGE, type, envelope);
    }
}
