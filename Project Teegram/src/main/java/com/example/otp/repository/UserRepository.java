package com.example.otp.repository;

import com.example.otp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);    // 👈 Вот этот метод
    Optional<User> findByUsername(String username); // 👈 и этот, нужен для логина
}
