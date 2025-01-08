package com.example.ebankify.repository;

import com.example.ebankify.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.id = :id")
    long countByRoleId(long id);
}
