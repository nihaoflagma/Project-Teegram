# Project-Teegram
---

## Teegram OTP Service

### О проекте

Telegram OTP Service — это backend-приложение, обеспечивающее безопасность операций с помощью одноразовых временных кодов (OTP). Сервис поддерживает отправку кодов через Email, SMS (эмулятор) и Telegram, а также проверку кода и настройку параметров его действия.

Сервис вдохновлён концепцией TOTP (Time-based One-Time Password).

---

### Функциональность

* Создание операций, защищённых OTP-кодом
* Генерация уникального кода
* Отправка кода:

  * по Email
  * в Telegram через бота
  * по SMS (эмулятор)
* Проверка кода
* Настройка времени действия и длины кода
* Сохранение OTP-кодов в файл
* Логирование всех операций
* Разграничение доступа по ролям

---

### Структура проекта

**Контроллеры:**

* `OtpController` — обработка REST-запросов на отправку и верификацию OTP.

**Сервисы:**

* `OtpCodeService`, `OtpVerificationService`, `OtpFileService`, `OtpCodeGenerator`
* `EmailNotificationService`, `SmsNotificationService`, `SmppNotificationService`, `TelegramNotificationService`
* `UserService`, `CustomUserDetailsService`

**DTO:**

* `LoginRequest`, `LoginResponse`, `OtpVerificationRequest`

**Безопасность:**

* `SecurityConfig` — настройка Spring Security, ограничение доступа по ролям
* Аутентификация через токен (JWT)

**Репозитории:**

* `OtpCodeRepository`, `UserRepository`, `RoleRepository`

**Инициализация:**

* `DataInitializer` — загрузка тестовых пользователей и ролей

**Модели:**

* `User`, `Role`, `OtpCode`

---

### Как пользоваться сервисом

#### Отправить OTP-код

`POST /api/otp/send`

Параметры (как query):

* `email`
* `telegramId`
* `phoneNumber`
* `channel`: `email` | `telegram` | `sms`

Пример:

```bash
curl -X POST "http://localhost:8080/api/otp/send?email=test@example.com&channel=email"
```

#### Проверить OTP-код

`POST /api/otp/verify`

Пример тела:

```json
{
  "email": "test@example.com",
  "code": "1234"
}
```

---

### Аутентификация и роли

Приложение использует JWT-токены для аутентификации и авторизации. В системе реализованы роли `USER` и `ADMIN`:

* Пользователь с ролью `USER` может отправлять и проверять OTP-коды.
* Пользователь с ролью `ADMIN` имеет доступ к расширенным настройкам, включая управление временем жизни и длиной кода.

Разграничение доступа реализовано в классе `SecurityConfig` с помощью аннотаций и конфигурации `HttpSecurity`.

---

### Логирование

Все входящие запросы логируются с помощью `Slf4j`:

* Метод запроса (GET, POST)
* Путь запроса
* Ключевые параметры (email, phone, telegramId)
* Статус отправки или проверки кода
* Результат верификации
* Время выполнения запроса

Это позволяет полностью отследить поток операций и упростить отладку.

---

### Сохранение в файл

Каждый сгенерированный OTP-код также сохраняется в файл `otp_codes.log`. Формат строки:

```
[2025-05-11 14:03:12] Email: test@example.com | Код: 4921 | Время действия: 5 минут
```

Это помогает при отладке и может использоваться для дополнительной безопасности и аудита.

---

### Пример `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/otp_service
spring.datasource.username=postgres
spring.datasource.password=your_password

email.username=your_email@mail.ru
email.password=your_password
email.from=your_email@mail.ru

telegram.bot.token=your_telegram_bot_token
```

---

### Сборка и запуск

```bash
./mvnw spring-boot:run
```

---

### Как тестировать

* Создайте базу PostgreSQL:

```sql
CREATE DATABASE otp_service;
```

* Укажите настройки в `application.properties`
* Запустите проект
* Проверьте работу через Postman или Telegram-бота

---

### О заказчике

**Promo IT** — компания, занимающаяся заказной разработкой ПО, аутсорсингом и аутстаффингом специалистов для крупных IT-клиентов. Основная деятельность: создание программных продуктов, предоставление IT-специалистов, поддержка корпоративных решений.

---

### Технологии

* Java 17+
* Spring Boot
* Spring Security
* PostgreSQL
* Jakarta Mail
* Telegram Bot API
* Lombok
* Maven

