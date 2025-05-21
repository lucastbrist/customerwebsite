package com.ltb.customerwebsite.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
@Builder
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullName;
    private String emailAddress;
    private Integer age;
    private String address;

    @OneToOne(mappedBy = "customer")
    private RentalCar rentalCar;
}
