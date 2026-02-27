package com.microservices.customerService.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Domain base entity: Person.
 * It owns the technical PK: personId.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Person {
    /**
     * Technical identifier (PK in DB). It might be null before persistence.
     */
    @EqualsAndHashCode.Include
    protected final PersonId personId;

    protected final String name;
    protected final String gender;
    protected final Integer age;
    protected final String identification;
    protected final String address;
    protected final String phone;

    protected Person(
            PersonId personId,
            String name,
            String gender,
            Integer age,
            String identification,
            String address,
            String phone
    ) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.personId = personId;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.identification = identification;
        this.address = address;
        this.phone = phone;
    }
}
