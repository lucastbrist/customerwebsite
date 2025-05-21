package com.ltb.customerwebsite.repositories;

import com.ltb.customerwebsite.models.RentalCar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalCarRepository extends
        JpaRepository<RentalCar, Long> {

    RentalCar findByCustomerId(Long id);

}
