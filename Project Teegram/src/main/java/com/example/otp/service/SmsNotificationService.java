package com.example.otp.service;

import org.springframework.stereotype.Service;

@Service
public class SmsNotificationService {

    public void sendCode(String phoneNumber, String code) {
        // Здесь пока имитация отправки SMS
        System.out.println("🚀 Отправка SMS:");
        System.out.println("Номер: " + phoneNumber);
        System.out.println("Код подтверждения: " + code);

        // В будущем здесь можно интегрировать реальный сервис типа Twilio, SMSC, etc.
    }
}
