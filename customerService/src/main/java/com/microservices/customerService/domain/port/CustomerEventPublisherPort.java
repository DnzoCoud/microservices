package com.microservices.customerService.domain.port;

import com.microservices.customerService.domain.model.Customer;
import com.microservices.customerService.domain.model.CustomerId;

public interface CustomerEventPublisherPort {
    void publishCreated(Customer customer);
    void publishUpdated(Customer customer);
    void publishDeleted(CustomerId customerId);
}
