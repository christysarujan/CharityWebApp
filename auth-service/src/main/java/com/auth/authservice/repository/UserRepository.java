package com.auth.authservice.repository;

import com.auth.authservice.entity.User;
import com.auth.authservice.util.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByRoles(Role role);
}
