package com.ltb.customerwebsite.controllers;

import com.ltb.customerwebsite.models.Customer;
import com.ltb.customerwebsite.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final JobLauncher jobLauncher;
    private final Job job;

    private final CustomerService customerService;

    @GetMapping("/")
    public String viewHomePage(Model model) {

        // call the service to retrieve all customers
        final List<Customer> customerList = customerService.getAllCustomers();

        // once the customers are retrieved, you can
        // store them in model and return the view
        model.addAttribute("customerList", customerList);

        return "index";
    }

    @GetMapping("/new")
    public String showNewCustomerPage(Model model) {

        // a new (empty) Customer is created and added to the model
        Customer customer = new Customer();
        model.addAttribute("customer", customer);

        // return the "new-customer" view
        return "new-customer";
    }

    @GetMapping("/edit/{id}")
// the path variable "id" is used to pull a customer from the database
    public ModelAndView showEditCustomerPage(
            @PathVariable(name = "id") Long id) {

// since the previous methods use Model, this one uses ModelAndView
// to get some experience using both. Model is more common these days,
// but ModelAndView accomplishes the same thing and can be useful in
// certain circumstances. The view name is passed to the constructor.

        ModelAndView mav = new ModelAndView("edit-customer");
        Customer customer = customerService.getCustomer(id);
        mav.addObject("customer", customer);
        return mav;
    }

    @PostMapping(value = "/save")
// As the Model is received back from the view, @ModelAttribute
// creates a Customer based on the object you collected from
// the HTML page above
    public String saveCustomer(
            @ModelAttribute("customer") Customer customer) {
        customerService.saveCustomer(customer);
        return "redirect:/";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(
            @PathVariable(name = "id") Long id,
            @ModelAttribute("customer") Customer customer, Model model) {

        if (!id.equals(customer.getId())) {
            model.addAttribute("message",
                    "Cannot update, customer id " + customer.getId()
                            + " doesn't match id to update: " + id + ".");
            return "error-page";
        }

        customerService.saveCustomer(customer);
        return "redirect:/";
    }

    @RequestMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id") Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/";
    }

    @PostMapping("/startJob")
    public String startJob(Model model) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(job, jobParameters);
            model.addAttribute("message", "Batch job started successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "Failed to start batch job: " + e.getMessage());
        }
        return "index";
    }


}
