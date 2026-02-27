package com.microservices.customerService.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customers", schema = "customer")
public class CustomerEntity {

    /**
     * Shared primary key with PersonEntity (person_id).
     */
    @Id
    @Column(name = "person_id")
    private Long personId;


    /**
     * One-to-one to PersonEntity using the same PK.
     */
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "person_id")
    private PersonEntity person;

    @Column(name = "customer_id", nullable = false, unique = true, length = 50)
    private String customerId;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(name = "status", nullable = false)
    private boolean status;
}
