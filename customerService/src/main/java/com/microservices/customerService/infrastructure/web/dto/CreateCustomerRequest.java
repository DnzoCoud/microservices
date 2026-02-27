package com.microservices.customerService.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerRequest {
    @NotBlank
    @Size(max = 50)
    private String customerId;

    @NotBlank
    @Size(max = 200)
    private String password;

    @NotNull
    private Boolean active;

    // Person
    @NotBlank
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
}
