package com.example.otp.controller;

import com.example.otp.dto.OtpVerificationRequest;
import com.example.otp.service.EmailNotificationService;
import com.example.otp.service.TelegramNotificationService;
import com.example.otp.service.SmppNotificationService;
import com.example.otp.service.OtpCodeService;
import com.example.otp.service.OtpFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {

    private final EmailNotificationService emailNotificationService;
    private final TelegramNotificationService telegramNotificationService;
    private final SmppNotificationService smppNotificationService;
    private final OtpCodeService otpService;
    private final OtpFileService otpFileService; // ➔ добавили сервис для записи в файл

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestParam String email,
                                          @RequestParam(required = false) String telegramId,
                                          @RequestParam(required = false) String phoneNumber,
                                          @RequestParam(defaultValue = "email") String channel) {
        log.info("Получен запрос на отправку OTP. Email: {}, TelegramId: {}, Phone: {}, Channel: {}", email, telegramId, phoneNumber, channel);

        String otp = otpService.generateOtp();
        otpFileService.saveOtpToFile(email, otp); // ➔ сохраняем в файл каждый сгенерированный код

        switch (channel.toLowerCase()) {
            case "email" -> {
                emailNotificationService.sendCode(email, otp);
                log.info("OTP отправлен на email: {}", email);
            }
            case "telegram" -> {
                if (telegramId == null || telegramId.isEmpty()) {
                    log.warn("Не указан Telegram ID");
                    return ResponseEntity.badRequest().body("Не указан Telegram ID");
                }
                telegramNotificationService.sendCode(telegramId, otp);
                log.info("OTP отправлен через Telegram: {}", telegramId);
            }
            case "sms" -> {
                if (phoneNumber == null || phoneNumber.isEmpty()) {
                    log.warn("Не указан номер телефона");
                    return ResponseEntity.badRequest().body("Не указан номер телефона");
                }
                smppNotificationService.sendCode(phoneNumber, otp);
                log.info("OTP отправлен через SMS: {}", phoneNumber);
            }
            default -> {
                log.error("Некорректный канал отправки: {}", channel);
                return ResponseEntity.badRequest().body("Некорректный канал отправки: email, telegram или sms");
            }
        }

        return ResponseEntity.ok("OTP код отправлен через " + channel);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
        log.info("Получен запрос на верификацию OTP для email: {}", request.getEmail());

        boolean isValid = otpService.verifyOtp(request.getEmail(), request.getCode());

        if (isValid) {
            log.info("OTP код успешно подтвержден для {}", request.getEmail());
            return ResponseEntity.ok("Код подтвержден успешно!");
        } else {
            log.warn("Ошибка подтверждения OTP кода для {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный или просроченный код.");
        }
    }
}
