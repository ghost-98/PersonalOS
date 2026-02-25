package com.example.backend.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entity.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailVerificationToken(String token);
}
