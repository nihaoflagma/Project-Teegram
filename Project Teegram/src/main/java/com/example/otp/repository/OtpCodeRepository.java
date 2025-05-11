package com.example.otp.repository;

import com.example.otp.model.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    
    Optional<OtpCode> findTopByEmailOrderByCreatedAtDesc(String email);

    
    Optional<OtpCode> findTopByTelegramIdOrderByCreatedAtDesc(String telegramId);

    
    Optional<OtpCode> findByEmailAndCodeAndUsedFalse(String email, String code);
}
