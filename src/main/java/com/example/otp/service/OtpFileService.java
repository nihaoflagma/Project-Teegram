package com.example.otp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class OtpFileService {

    private static final String FILE_NAME = "otp-codes.txt";

    public void saveOtpToFile(String email, String code) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String record = String.format("Email: %s | Code: %s | Created At: %s%n", email, code, timestamp);

        log.info("Попытка сохранить OTP в файл: {}", FILE_NAME); // добавляем лог до открытия файла

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(record);
            log.info("OTP код сохранен в файл для email: {}", email);
        } catch (IOException e) {
            log.error("Ошибка при записи OTP в файл: {}", e.getMessage(), e);
        }
    }
}