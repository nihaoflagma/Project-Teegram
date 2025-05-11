package com.example.otp.service;

import com.example.otp.model.OtpCode;
import com.example.otp.repository.OtpCodeRepository;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final OtpCodeRepository otpRepository;

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    @Value("${email.from}")
    private String fromEmail;

    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private int smtpPort;

    @Value("${mail.smtp.auth:true}")
    private boolean smtpAuth;

    @Value("${mail.smtp.starttls.enable:true}")
    private boolean starttlsEnable;

    private Session createSession() {
        Properties config = new Properties();
        config.put("mail.smtp.auth", String.valueOf(smtpAuth));
        config.put("mail.smtp.starttls.enable", String.valueOf(starttlsEnable));
        config.put("mail.smtp.host", smtpHost);
        config.put("mail.smtp.port", String.valueOf(smtpPort));

        return Session.getInstance(config, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendCode(String toEmail, String code) {
        try {
            Message message = new MimeMessage(createSession());
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Ваш код подтверждения");
            message.setText("Ваш код подтверждения: " + code);

            Transport.send(message);
            System.out.println("✅ Email успешно отправлен!");

            saveOtpCode(toEmail, code);

        } catch (MessagingException e) {
            throw new RuntimeException("❌ Ошибка при отправке email", e);
        }
    }

    private void saveOtpCode(String email, String code) {
        OtpCode otpCode = OtpCode.builder()
                .email(email)
                .code(code)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .build();

        otpRepository.save(otpCode);
    }
}
