package com.example.ebankify.repository;

import com.example.ebankify.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<Object> findByName(String name);
}
