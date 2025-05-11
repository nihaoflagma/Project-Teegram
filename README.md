# Project-Teegram
Teegram OTP Service

О проекте

Telegram OTP Service — это backend-приложение, обеспечивающее безопасность операций с помощью одноразовых временных кодов (OTP). Сервис поддерживает отправку кодов через Email, SMS (эмулятор) и Telegram, а также проверку кода и настройку параметров его действия.

Функциональность

Создание операции, защищённой OTP-кодом

Генерация уникального кода

Отправка кода:

по Email

в Telegram через бота

по SMS (эмулятор)

Проверка кода

Настройка времени действия и длины кода

Сервис вдохновлён концепцией TOTP (Time-based One-Time Password).

Структура проекта

Контроллеры

OtpController — обработка REST-запросов на отправку и верификацию OTP.

Сервисы

OtpCodeService, OtpVerificationService, OtpFileService, OtpCodeGenerator

EmailNotificationService, SmsNotificationService, SmppNotificationService, TelegramNotificationService

UserService, CustomUserDetailsService

DTO

LoginRequest, LoginResponse, OtpVerificationRequest

Безопасность

SecurityConfig

Репозитории

OtpCodeRepository, UserRepository, RoleRepository

Инициализация

DataInitializer

Модели

User, Role, OtpCode

Как пользоваться сервисом

1. Отправить OTP-код

POST /api/otp/send

Параметры:

email

telegramId

phoneNumber

channel: email | telegram | sms

Пример:

curl -X POST "http://localhost:8080/api/otp/send?email=test@example.com&channel=email"

2. Проверить OTP-код

POST /api/otp/verify

Тело:

{
  "email": "test@example.com",
  "code": "1234"
}

Как тестировать

Создайте базу PostgreSQL:

CREATE DATABASE otp_service;

Укажите в application.properties настройки для почты, телеграма и БД

Сборка и запуск:

./mvnw spring-boot:run

Тест через Postman или Telegram Bot

Пример application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/otp_service
spring.datasource.username=postgres
spring.datasource.password=your_password

email.username=your_email@mail.ru
email.password=your_password
email.from=your_email@mail.ru

telegram.bot.token=your_telegram_bot_token

О заказчике

Promo IT — компания, занимающаяся заказной разработкой ПО, аутсорсингом и аутстаффингом специалистов для крупных IT-клиентов.

Технологии

Java 17+

Spring Boot

Spring Security

PostgreSQL

Jakarta Mail

Telegram Bot API

Lombok

Maven
