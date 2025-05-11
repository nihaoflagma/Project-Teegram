package com.example.otp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmppNotificationService {

    public void sendCode(String phoneNumber, String code) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            log.error("Номер телефона не указан для отправки OTP через SMPP");
            throw new IllegalArgumentException("Номер телефона не может быть пустым");
        }

        log.info("Фейковая отправка OTP кода [{}] на номер [{}] через эмулятор SMPP", code, phoneNumber);

        try {
            // Небольшая имитация задержки отправки
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Ошибка при имитации отправки SMS через SMPP", e);
        }
    }
}
