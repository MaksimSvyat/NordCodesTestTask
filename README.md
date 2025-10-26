# Тестовый проект для AQA Engineer
Автоматизированные тесты для Spring Boot веб-сервиса с одним эндпоинтом, обрабатывающим действия: LOGIN, ACTION, LOGOUT.

## 🚀 Быстрый старт

### 1. Установите тестовое приложение
```bash
wget https://gametests.nyc.wf/aga.7z
7z x aga.7z -p"g7%Kp9#rX2bl"
```

### 2. Запустите приложение
```bash
java -jar -Dsecret=qazWSXedc -Dmock=http://localhost:8888/ internal-0.0.1-SNAPSHOT.jar
```

### 3. Запустите тесты с отчетом
```bash
mvn clean test allure:serve
```

## 🛠 Технологии

- **Java 17**, **JUnit 5**, **Maven**
- **WireMock** - мокирование внешних сервисов
- **Allure** - отчетность, **REST Assured** - HTTP-клиент
- **JavaFaker** - генерация тестовых данных

## 📁 Архитектура проекта

```
src/test/java/
├── builders/          # Builder паттерн для тестовых данных
├── clients/           # HTTP-клиенты
├── config/            # Конфигурация
├── helpers/           # Утилиты (генерация токенов)
├── mock/              # WireMock моки
├── steps/             # Allure-шаги
└── tests/             # Тестовые классы
```

## 🧪 Тестовое покрытие

### Основные сценарии:
- **Полный цикл**: LOGIN → ACTION → LOGOUT
- **Валидация**: токены (длина, символы), API-ключ
- **Ошибки**: внешние сервисы, некорректные данные
- **Производительность**: время ответа < 2с

### Группы тестов:
- **Smoke** - критичная функциональность
- **Параметризованные** - граничные значения
- **Интеграционные** - работа с моками

## ⚙️ Конфигурация

`src/test/resources/application.properties`:
```properties
app.base.url=http://localhost:8080
app.api.key=qazWSXedc
app.endpoint=/endpoint

mock.port=8888
mock.auth.endpoint=/auth
mock.action.endpoint=/doAction

test.token.length=32
token.valid.chars=ABCDEF0123456789
```

## 🎯 Ключевые особенности

### Fluent-интерфейс построения запросов:
```java
var request = RequestDataBuilder.create()
    .withValidToken().withLoginAction().build();
```

### Мокирование внешних сервисов:
```java
MockService.stubAuthEndpoint(200); // Успех
MockService.stubAuthEndpoint(500); // Ошибка
```

### Детальная Allure-отчетность:
- Параметры запросов и ответов
- Временные метки выполнения
- Структурированные шаги тестов

## 📝 Важные примечания

### Формат токенов
По умолчанию: **A-F0-9** (hex). Для соответствия ТЗ (**A-Z0-9**) измените:
```properties
token.valid.chars=ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789
```

### Проблемы при запуске
**Порт занят?** Используйте другой порт:
```bash
java -jar -Dserver.port=8081 -Dsecret=qazWSXedc -Dmock=http://localhost:8888/ internal-0.0.1-SNAPSHOT.jar
```
и обновите `app.base.url` в properties.

**Ошибки подключения?** Проверьте:
- Приложение запущено на `localhost:8080`
- Порты 8080 и 8888 свободны
- API ключ: `qazWSXedc`

## 🔧 Команды Maven

```bash
# Все тесты
mvn clean test

# Только smoke-тесты
mvn clean test -Dgroups=smoke

# Конкретный тестовый класс
mvn clean test -Dtest=ApplicationTests

# Allure отчет
mvn allure:serve
```
