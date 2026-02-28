package com.microservices.customerService.infrastructure.persistence.repository;

import com.microservices.customerService.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByCustomerIdAndStatusTrue(String customerId);
    boolean existsByCustomerId(String customerId);
    void deleteByCustomerId(String customerId);
    List<CustomerEntity> findAllBytatusTrue();
}
