package com.example.otp.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpCodeGenerator {

    public String generateCode() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); 
        return String.valueOf(otp);
    }
}
