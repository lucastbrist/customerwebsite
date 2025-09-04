package com.ltb.customerwebsite;

import com.ltb.customerwebsite.models.Role;
import com.ltb.customerwebsite.models.User;
import com.ltb.customerwebsite.repositories.RoleRepository;
import com.ltb.customerwebsite.repositories.UserRepository;
import com.ltb.customerwebsite.services.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;

@SpringBootApplication
public class CustomerWebsiteApplication implements CommandLineRunner {

	@Autowired
	private CustomerServiceImpl customerServiceImpl;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initializer(RoleRepository roleRepository, UserRepository userRepository) {
		return args -> {
			// Check if the RoleRepository is empty
			if (roleRepository.findAll().isEmpty()) {
				// if it is, persist ROLE_USER and ROLE_ADMIN
				roleRepository.save(new Role(Role.Roles.ROLE_USER));
				roleRepository.save(new Role(Role.Roles.ROLE_ADMIN));
			}
			// Check if the UserRepository is empty
			if (userRepository.findAll().isEmpty()) {
				// If it is, persist one user with the ADMIN role
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("adminPassword"));
				Collection<Role> adminAuthorities = new ArrayList<>();
				adminAuthorities.add(roleRepository.findByRole(Role.Roles.ROLE_ADMIN));
				adminAuthorities.add(roleRepository.findByRole(Role.Roles.ROLE_USER));
				admin.setAuthorities(adminAuthorities);

				userRepository.save(admin);
			}
		};
    }

	// The main method is defined here which will start your application.
	public static void main(String[] args) {
		SpringApplication.run(CustomerWebsiteApplication.class);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
