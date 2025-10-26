package tests;

import builders.RequestDataBuilder;
import helpers.TokenHelper;
import io.qameta.allure.*;
import mock.MockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;
import static steps.ApplicationSteps.*;

@Epic("Тестирование Spring Boot приложения")
@Feature("Основной эндпоинт /endpoint")
public class ApplicationTests extends BaseTest {

    @Test
    @DisplayName("Успешная последовательность: LOGIN -> ACTION -> LOGOUT")
    @Story("Пользователь выполняет полный цикл действий")
    @Severity(SeverityLevel.CRITICAL)
    void successfulFullFlow() {
        String validToken = TokenHelper.generateValidToken();
        executeFullFlow(validToken);
    }

    @Test
    @DisplayName("Отсутствие API ключа возвращает ошибку")
    @Story("Проверка авторизации")
    @Severity(SeverityLevel.CRITICAL)
    void missingApiKeyReturnsError() {
        var requestData = RequestDataBuilder.create()
                .withValidToken()
                .withLoginAction()
                .withoutApiKey()
                .build();
        var response = executeRequest(requestData);
        verifyAuthError(response);
    }

    @Test
    @DisplayName("Ошибка внешнего сервиса при AUTH")
    @Story("Обработка ошибок внешних зависимостей")
    @Severity(SeverityLevel.NORMAL)
    void externalAuthServiceError() {
        String token = TokenHelper.generateValidToken();
        MockService.stubAuthEndpoint(500);
        var response = executeLogin(token);
        verifyExternalServiceError(response);
    }

    @Test
    @DisplayName("Ошибка внешнего сервиса при ACTION")
    @Story("Обработка ошибок внешних зависимостей")
    @Severity(SeverityLevel.NORMAL)
    void externalActionServiceError() {
        String token = TokenHelper.generateValidToken();
        executeLogin(token);
        MockService.stubDoActionEndpoint(500);
        var response = executeAction(token);
        verifyExternalServiceError(response);
    }

    @Test
    @Tag("smoke")
    @DisplayName("Проверка всех обязательных сценариев из ТЗ")
    @Story("Полное покрытие требований")
    @Severity(SeverityLevel.CRITICAL)
    void comprehensiveTZCoverageTest() {
        String validToken = TokenHelper.generateValidToken();

        // 1. Валидный LOGIN
        var loginResponse = executeLogin(validToken);
        verifySuccessResponse(loginResponse);

        // 2. Валидный ACTION после LOGIN
        var actionResponse = executeAction(validToken);
        verifySuccessResponse(actionResponse);

        // 3. Валидный LOGOUT
        var logoutResponse = executeLogout(validToken);
        verifySuccessResponse(logoutResponse);

        // 4. Невалидный токен возвращает ошибку
        var invalidTokenRequest = RequestDataBuilder.create()
                .withInvalidToken()
                .withLoginAction()
                .build();
        var invalidTokenResponse = executeRequest(invalidTokenRequest);
        verifyValidationError(invalidTokenResponse);

        // 5. ACTION без LOGIN возвращает ошибку
        var newToken = TokenHelper.generateValidToken();
        var actionWithoutLoginResponse = executeAction(newToken);
        verifyForbiddenError(actionWithoutLoginResponse);
    }

    @Test
    @DisplayName("Время ответа менее 2 секунд")
    @Story("Требования к производительности")
    @Severity(SeverityLevel.NORMAL)
    void responseTimeShouldBeReasonable() {
        var response = executeLogin(TokenHelper.generateValidToken());
        response.then().time(lessThan(2000L), TimeUnit.MILLISECONDS);
    }
}