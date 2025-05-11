package com.example.otp.service;

import org.springframework.stereotype.Service;

@Service
public class SmsNotificationService {

    public void sendCode(String phoneNumber, String code) {
        
        System.out.println("🚀 Отправка SMS:");
        System.out.println("Номер: " + phoneNumber);
        System.out.println("Код подтверждения: " + code);

        
    }
}
