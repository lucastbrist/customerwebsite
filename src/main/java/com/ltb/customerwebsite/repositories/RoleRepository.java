package com.ltb.customerwebsite.repositories;

import com.ltb.customerwebsite.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(Role.Roles role);
}
