package com.microservices.customerService.infrastructure.messaging.dto;

import java.time.Instant;

public record CustomerEventEnvelope(
        String eventId,
        String type,
        int version,
        Instant occurredAt,
        CustomerEventData data
) { }
