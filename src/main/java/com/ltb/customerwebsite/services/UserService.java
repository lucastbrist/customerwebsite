package com.ltb.customerwebsite.services;

import com.ltb.customerwebsite.models.Role;
import com.ltb.customerwebsite.models.User;
import com.ltb.customerwebsite.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User optionalUser =
                userRepository.findByUsername(username);

        if (optionalUser == null) {
            throw new UsernameNotFoundException(username +
                    " is not a valid username! " +
                    "Check for typos and try again.");
        }

        return optionalUser;
    }

    @Transactional(readOnly = true)
    public User getUserByUserId(Long userId)
            throws EntityNotFoundException {

        Optional<User> optional = userRepository.findById(userId);
        User user;
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("User not found.");
        } else {
            user = optional.get();
        }

        // call unproxy() to ensure all related entities
        // are loaded—no lazy load exceptions.
        return (User) Hibernate.unproxy(user);
    }

    @Transactional(readOnly = true)
    public User getUser(String username)
            throws EntityNotFoundException  {

        return userRepository.findByUsername(username);
    }

    public User createNewUser(
            User userDetails) {
        userDetails.setId(null);
        userDetails.getAuthorities().forEach(a -> a.setId(null));

        //override or set user settings to correct values
        userDetails.setAccountNonExpired(true);
        userDetails.setAccountNonLocked(true);
        userDetails.setCredentialsNonExpired(true);
        userDetails.setEnabled(true);
        userDetails.setAuthorities(
                Collections.singletonList(
                        new Role(Role.Roles.ROLE_USER)));

        checkPassword(userDetails.getPassword());
        userDetails.setPassword(
                encoder.encode(userDetails.getPassword()));
        try {
            return userRepository.save(userDetails);
        } catch (Exception e) {
            throw new IllegalStateException(
                    e.getMessage(), e.getCause());
        }
    }

    private void checkPassword(String password) {
        if (password == null) {
            throw new IllegalStateException("You must set a password");
        }
        if (password.length() < 6) {
            throw new IllegalStateException(
                    "Password is too short. " +
                            "Must be longer than 6 characters");
        }
    }
}
