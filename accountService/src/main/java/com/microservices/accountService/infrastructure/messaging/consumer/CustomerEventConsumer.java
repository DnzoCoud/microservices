package com.microservices.accountService.infrastructure.messaging.consumer;

import com.microservices.accountService.domain.model.CustomerSnapshot;
import com.microservices.accountService.domain.port.CustomerSnapshotRepositoryPort;
import com.microservices.accountService.infrastructure.messaging.config.RabbitConsumerConfig;
import com.microservices.accountService.infrastructure.messaging.dto.CustomerEventEnvelope;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomerEventConsumer {
    private final CustomerSnapshotRepositoryPort snapshotRepo;

    public CustomerEventConsumer(CustomerSnapshotRepositoryPort snapshotRepo) {
        this.snapshotRepo = snapshotRepo;
    }

    @Transactional
    @RabbitListener(
            queues = RabbitConsumerConfig.CUSTOMER_QUEUE,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void onEvent(CustomerEventEnvelope evt) {
        if (evt == null || evt.type() == null || evt.data() == null) return;

        String type = evt.type();
        var data = evt.data();


        switch (type) {
            case "customer.created", "customer.updated" -> {
                snapshotRepo.upsert(CustomerSnapshot.builder()
                    .customerId(data.customerId())
                    .name(data.name()).active(data.status())
                    .build());
            }
            case "customer.deleted" -> snapshotRepo.upsert(CustomerSnapshot.builder()
                    .customerId(data.customerId())
                    .name(data.name())
                    .active(false)
                    .build());
            default -> {}
        }
    }
}
