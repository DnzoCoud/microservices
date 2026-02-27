package com.microservices.customerService.infrastructure.web.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCustomerRequest {
    @Size(max = 120)
    private String name;

    @Size(max = 20)
    private String gender;

    private Integer age;

    @Size(max = 50)
    private String identification;

    @Size(max = 200)
    private String address;

    @Size(max = 40)
    private String phone;

    @Size(max = 200)
    private String password;

    private Boolean active;
}
