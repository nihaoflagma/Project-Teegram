package com.example.otp.service;

import com.example.otp.model.OtpCode;
import com.example.otp.repository.OtpCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpCodeService {

    private final OtpCodeRepository otpCodeRepository;

    
    public String generateOtp() {
        int otp = (int) (Math.random() * 9000) + 1000; 
        return String.valueOf(otp);
    }

    
    public void saveOtp(String email, String otpCode) {
        OtpCode otp = new OtpCode();
        otp.setEmail(email);
        otp.setCode(otpCode);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5)); 
        otp.setUsed(false);

        otpCodeRepository.save(otp);
    }

    
    public boolean verifyOtp(String email, String code) {
        Optional<OtpCode> otpOptional = otpCodeRepository.findByEmailAndCodeAndUsedFalse(email, code);

        if (otpOptional.isEmpty()) {
            return false;
        }

        OtpCode otp = otpOptional.get();

        
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        
        otp.setUsed(true);
        otpCodeRepository.save(otp);

        return true;
    }
}
