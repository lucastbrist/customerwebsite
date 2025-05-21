package com.ltb.customerwebsite.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rental_cars")
@Builder
@Getter
@Setter
public class RentalCar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String make;
    private String model;
    private String color;
    private Integer year;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
