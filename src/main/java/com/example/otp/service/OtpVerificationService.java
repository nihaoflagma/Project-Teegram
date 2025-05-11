package com.example.otp.service;

import com.example.otp.model.OtpCode;
import com.example.otp.repository.OtpCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class OtpVerificationService {

    @Autowired
    private OtpCodeRepository otpCodeRepository;

    // Метод для верификации OTP
    public boolean verifyOtp(String email, String code) {
        // Ищем последний OTP код по email, не использованный
        Optional<OtpCode> otpCodeOptional = otpCodeRepository.findByEmailAndCodeAndUsedFalse(email, code);

        if (otpCodeOptional.isPresent()) {
            OtpCode otpCode = otpCodeOptional.get();

            // Проверка: код истек или нет
            if (otpCode.getExpiresAt().isBefore(LocalDateTime.now())) {
                log.warn("OTP код для email: {} истек!", email);
                return false;
            }

            // Проверка: был ли использован код
            if (otpCode.isUsed()) {
                log.warn("OTP код для email: {} уже использован!", email);
                return false;
            }

            // Если код правильный, помечаем как использованный
            otpCode.setUsed(true);
            otpCodeRepository.save(otpCode);

            log.info("OTP код для email: {} успешно подтвержден", email);
            return true;
        }

        log.warn("Неверный OTP код для email: {}", email);
        return false;
    }
}
