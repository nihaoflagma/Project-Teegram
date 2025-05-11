package com.example.otp.dto;

import lombok.Data;

@Data
public class OtpVerificationRequest {
    private String email;
    private String code;
}
