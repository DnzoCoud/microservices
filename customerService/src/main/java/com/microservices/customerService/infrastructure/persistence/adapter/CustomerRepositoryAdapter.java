package com.microservices.customerService.infrastructure.persistence.adapter;

import com.microservices.customerService.domain.model.Customer;
import com.microservices.customerService.domain.model.CustomerId;
import com.microservices.customerService.domain.port.CustomerRepositoryPort;
import com.microservices.customerService.infrastructure.persistence.mapper.CustomerPersistenceMapper;
import com.microservices.customerService.infrastructure.persistence.repository.CustomerJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {
    private final CustomerJpaRepository jpa;
    private final CustomerPersistenceMapper mapper;

    public CustomerRepositoryAdapter(CustomerJpaRepository jpa, CustomerPersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }


    @Override
    public Customer save(Customer customer) {
        return mapper.toDomain(jpa.save(mapper.toEntity(customer)));
    }

    @Override
    public Optional<Customer> findByCustomerId(CustomerId customerId) {
        return jpa.findByCustomerId(customerId.getValue()).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCustomerId(CustomerId customerId) {
        return jpa.existsByCustomerId(customerId.getValue());
    }

    @Override
    public List<Customer> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteByCustomerId(CustomerId customerId) {
        jpa.deleteByCustomerId(customerId.getValue());
    }
}
