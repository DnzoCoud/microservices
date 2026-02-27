package com.microservices.customerService.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "persons", schema = "customer")
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long personId;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "identification", length = 50)
    private String identification;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "phone", length = 40)
    private String phone;
}
