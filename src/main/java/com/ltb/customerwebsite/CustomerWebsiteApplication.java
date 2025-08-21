package com.ltb.customerwebsite;

import com.ltb.customerwebsite.models.Customer;
import com.ltb.customerwebsite.services.CustomerService;
import com.ltb.customerwebsite.services.CustomerServiceImpl;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class CustomerWebsiteApplication implements CommandLineRunner {

	@Autowired
	private CustomerServiceImpl customerServiceImpl;

	// The main method is defined here which will start your application.
	public static void main(String[] args) {
		SpringApplication.run(CustomerWebsiteApplication.class);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
