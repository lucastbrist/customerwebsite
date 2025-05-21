package com.ltb.customerwebsite.controllers;

import com.ltb.customerwebsite.models.Customer;
import com.ltb.customerwebsite.models.RentalCar;
import com.ltb.customerwebsite.services.CustomerService;
import com.ltb.customerwebsite.services.RentalCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RentalCarController {

    @Autowired
    RentalCarService rentalCarService;

    @Autowired
    CustomerService customerService;

    @GetMapping("/cars")
    public String viewHomePage(Model model) {

        final List<RentalCar> rentalCars = rentalCarService.getAllRentalCars();
        model.addAttribute("rentalCars", rentalCars);
        return "cars";

    }

    @GetMapping("/new-car")
    public String showNewCarPage(Model model) {

        RentalCar rentalCar = new RentalCar();
        model.addAttribute("rentalCar", rentalCar);
        return "new-car";

    }

    @PostMapping("/cars")
    public String saveCar(@ModelAttribute("rentalCar") RentalCar rentalCar, Model model) {

        try {
            rentalCarService.saveRentalCar(rentalCar);
        } catch (IllegalArgumentException e) {
            model.addAttribute("message","Could not save car," + e.getMessage());
            return "error-page";
        }

        return "redirect:/cars";

    }

    @RequestMapping("/remove/{id}")
    public String removeCar(@PathVariable(name = "id") Long id) {

        RentalCar rentalCar = rentalCarService.getRentalCar(id);
        rentalCar.setCustomer(null);
        rentalCarService.saveRentalCar(rentalCar);
        return "redirect:/";

    }

    @GetMapping("/cars/assign/{id}")
    public String assignCar(@PathVariable(name = "id") Long id, Model model) {

        Customer customer = customerService.getCustomer(id);
        List<RentalCar> rentalCars = rentalCarService.getAvailableRentalCars();
        model.addAttribute("customer",customer);
        model.addAttribute("rentalCars",rentalCars);
        return "assign-car";

    }

    @PostMapping("/cars/assign")
    public String saveCarAssignment(@RequestParam("customerId") Long customerId, @RequestParam("carId") Long carId) {

        RentalCar rentalCar = rentalCarService.getRentalCar(carId);
        rentalCar.setCustomer(customerService.getCustomer(customerId));
        rentalCarService.saveRentalCar(rentalCar);
        return "redirect:/";


    }

}