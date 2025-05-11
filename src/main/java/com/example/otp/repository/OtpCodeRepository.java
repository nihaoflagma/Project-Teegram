package com.example.otp.repository;

import com.example.otp.model.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    // Найти последний код подтверждения по email
    Optional<OtpCode> findTopByEmailOrderByCreatedAtDesc(String email);

    // Найти последний код подтверждения по Telegram ID
    Optional<OtpCode> findTopByTelegramIdOrderByCreatedAtDesc(String telegramId);

    // Новый метод: найти код по email, code и только если он не использован
    Optional<OtpCode> findByEmailAndCodeAndUsedFalse(String email, String code);
}
