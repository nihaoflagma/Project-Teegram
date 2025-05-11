package com.example.otp.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Slf4j
@Service
public class TelegramNotificationService {

    private final String token;
    private final String defaultChatId;

    public TelegramNotificationService() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("telegram.properties")) {
            if (input == null) {
                throw new RuntimeException("Не найден файл telegram.properties");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить telegram.properties", e);
        }

        this.token = props.getProperty("telegram.bot.token");
        this.defaultChatId = props.getProperty("telegram.chat.id");

        if (this.token == null || this.defaultChatId == null) {
            throw new IllegalArgumentException("Telegram token или chat ID не найдены в конфигурации.");
        }
    }

    public void sendCode(String telegramId, String code) {
        String chatIdToUse = (telegramId != null && !telegramId.isEmpty()) ? telegramId : defaultChatId;

        String message = "Ваш код подтверждения: " + code;
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

        String url = String.format(
                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                token, chatIdToUse, encodedMessage
        );

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            log.info("Отправляем запрос в Telegram: {}", url);
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

                if (statusCode != 200) {
                    log.error("Ошибка при отправке в Telegram: HTTP {}", statusCode);
                    log.error("Ответ Telegram: {}", responseBody);
                } else {
                    log.info("Код успешно отправлен в Telegram. Ответ: {}", responseBody);
                }
            }
        } catch (IOException e) {
            log.error("Ошибка при работе с Telegram API: {}", e.getMessage(), e);
        }
    }
}
