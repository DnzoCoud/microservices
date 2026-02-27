package com.microservices.accountService.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer_snapshot", schema = "account")
public class CustomerSnapshotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false, unique = true, length = 50)
    private String customerId;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
