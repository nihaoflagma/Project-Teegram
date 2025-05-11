package com.example.otp;

import com.example.otp.model.OtpCode;
import com.example.otp.repository.OtpCodeRepository;
import com.example.otp.service.EmailNotificationService;
import com.example.otp.service.TelegramNotificationService;
import com.example.otp.util.OtpCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);

        
        OtpCodeGenerator generator = context.getBean(OtpCodeGenerator.class);
        String code = generator.generateCode();

        
        EmailNotificationService emailService = context.getBean(EmailNotificationService.class);
        emailService.sendCode("vladvladgg2000@mail.ru", code);

        
        TelegramNotificationService telegramService = context.getBean(TelegramNotificationService.class);
        telegramService.sendCode("871237277", code);

        
        OtpCodeRepository repository = context.getBean(OtpCodeRepository.class);
        OtpCode otpCode = OtpCode.builder()
                .email("vladvladgg2000@mail.ru")
                .telegramId(null)
                .code(code)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .build();

        repository.save(otpCode);

        
        com.example.otp.service.OtpFileService otpFileService = context.getBean(com.example.otp.service.OtpFileService.class);
        otpFileService.saveOtpToFile("vladvladgg2000@mail.ru", code);

        System.out.println("✅ OTP код успешно сохранен в базу данных и записан в файл!");
    }
}

