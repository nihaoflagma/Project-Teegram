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

    
    public boolean verifyOtp(String email, String code) {
        
        Optional<OtpCode> otpCodeOptional = otpCodeRepository.findByEmailAndCodeAndUsedFalse(email, code);

        if (otpCodeOptional.isPresent()) {
            OtpCode otpCode = otpCodeOptional.get();

            
            if (otpCode.getExpiresAt().isBefore(LocalDateTime.now())) {
                log.warn("OTP код для email: {} истек!", email);
                return false;
            }

            
            if (otpCode.isUsed()) {
                log.warn("OTP код для email: {} уже использован!", email);
                return false;
            }

            
            otpCode.setUsed(true);
            otpCodeRepository.save(otpCode);

            log.info("OTP код для email: {} успешно подтвержден", email);
            return true;
        }

        log.warn("Неверный OTP код для email: {}", email);
        return false;
    }
}
