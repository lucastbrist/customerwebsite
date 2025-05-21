package com.ltb.customerwebsite.services;
import com.ltb.customerwebsite.models.RentalCar;
import com.ltb.customerwebsite.repositories.RentalCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalCarService {

    @Autowired
    RentalCarRepository rentalCarRepository;

    public List<RentalCar> getAllRentalCars() {

        return rentalCarRepository.findAll();

    }

    @Transactional
    public RentalCar saveRentalCar(RentalCar rentalCar) throws IllegalArgumentException {

        return rentalCarRepository.save(rentalCar);

    }

    public RentalCar getRentalCar(Long id) {

        return rentalCarRepository.getById(id);

    }

    public void deleteRentalCar(Long id) {

        rentalCarRepository.delete(rentalCarRepository.getById(id));

    }

    public void saveAllRentalCars(List<RentalCar> rentalCarList) {

        rentalCarRepository.saveAll(rentalCarList);

    }

    public List<RentalCar> getAvailableRentalCars() {

        return getAllRentalCars().stream().filter(c -> c.getCustomer() == null)
                .collect(Collectors.toList());

    }
}
