package com.microservices.customerService.infrastructure.persistence.mapper;

import com.microservices.customerService.domain.model.Customer;
import com.microservices.customerService.domain.model.CustomerId;
import com.microservices.customerService.domain.model.PersonId;
import com.microservices.customerService.infrastructure.persistence.entity.CustomerEntity;
import com.microservices.customerService.infrastructure.persistence.entity.PersonEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceMapper {
    public Customer toDomain(CustomerEntity e) {
        if (e == null) return null;

        PersonEntity p = e.getPerson();

        return Customer.builder()
                .personId(e.getPersonId() != null ? PersonId.of(e.getPersonId()) : null)
                .name(p.getName())
                .gender(p.getGender())
                .age(p.getAge())
                .identification(p.getIdentification())
                .address(p.getAddress())
                .phone(p.getPhone())
                .customerId(CustomerId.of(e.getCustomerId()))
                .passwordHash(e.getPassword())
                .active(e.isStatus())
                .build();
    }

    /**
     * Builds a persistence graph that matches the shared PK mapping:
     * CustomerEntity -> PersonEntity (cascade ALL).
     */
    public CustomerEntity toEntity(Customer d) {
        if (d == null) return null;

        PersonEntity person = PersonEntity.builder()
                .personId(d.getPersonId() != null ? d.getPersonId().getValue() : null)
                .name(d.getName())
                .gender(d.getGender())
                .age(d.getAge())
                .identification(d.getIdentification())
                .address(d.getAddress())
                .phone(d.getPhone())
                .build();

        return CustomerEntity.builder()
                .personId(d.getPersonId() != null ? d.getPersonId().getValue() : null)
                .person(person)
                .customerId(d.getCustomerId().getValue())
                .password(d.getPasswordHash())
                .status(d.isActive())
                .build();
    }
}
