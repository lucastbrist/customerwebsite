package com.ltb.customerwebsite.repositories;

import com.ltb.customerwebsite.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
        extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
