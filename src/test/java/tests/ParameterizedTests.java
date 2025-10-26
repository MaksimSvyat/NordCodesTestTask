package tests;

import builders.RequestDataBuilder;
import helpers.TokenHelper;
import io.qameta.allure.*;
import mock.MockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Random;
import java.util.stream.Stream;

import static steps.ApplicationSteps.*;

@Epic("Параметризованное тестирование")
@Feature("Валидация входных параметров")
public class ParameterizedTests extends BaseTest {

    private final Random random = new Random();

    @Nested
    @DisplayName("Валидация действий (action)")
    class ActionValidationTests {

        @ParameterizedTest
        @ValueSource(strings = {"LOGIN", "ACTION", "LOGOUT"})
        @DisplayName("Валидные действия с неправильным контекстом")
        @Story("Проверка корректных действий в неправильном состоянии")
        @Severity(SeverityLevel.CRITICAL)
        void validActionsWithWrongContext(String action) {
            var requestData = RequestDataBuilder.create()
                    .withValidToken()
                    .withAction(action)
                    .build();

            var response = executeRequest(requestData);

            if ("LOGIN".equals(action)) {
                verifySuccessResponse(response);
            } else {
                verifyForbiddenError(response);
            }
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   ", "INVALID_ACTION", "UNKNOWN", "TEST", "LOGIN_ACTION"})
        @DisplayName("Невалидные действия возвращают ошибку")
        @Story("Валидация некорректных действий")
        @Severity(SeverityLevel.CRITICAL)
        void invalidActionsShouldFail(String action) {
            var requestData = RequestDataBuilder.create()
                    .withValidToken()
                    .withAction(action)
                    .build();

            var response = executeRequest(requestData);
            verifyValidationError(response);
        }
    }

    @Nested
    @DisplayName("Валидация токенов")
    class TokenValidationTests {

        @ParameterizedTest
        @MethodSource("provideInvalidTokens")
        @DisplayName("Разные типы невалидных токенов")
        @Story("Валидация некорректных токенов")
        @Severity(SeverityLevel.CRITICAL)
        void differentInvalidTokensShouldFail(String token) {
            var requestData = RequestDataBuilder.create()
                    .withToken(token)
                    .withLoginAction()
                    .build();

            var response = executeRequest(requestData);
            verifyValidationError(response);
        }

        @ParameterizedTest
        @MethodSource("provideTokenLengths")
        @DisplayName("Валидация длины токена")
        @Story("Проверка граничных значений длины токена")
        @Severity(SeverityLevel.CRITICAL)
        void tokenLengthValidation(int length, boolean shouldBeValid) {
            String token = generateTokenOfLength(length);

            var response = executeLogin(token);

            if (shouldBeValid) {
                verifySuccessResponse(response);
            } else {
                verifyValidationError(response);
            }
        }

        private static Stream<Arguments> provideInvalidTokens() {
            return Stream.of(
                    Arguments.of((Object) null),
                    Arguments.of(""),
                    Arguments.of("ABC"),
                    Arguments.of("A".repeat(100)),
                    Arguments.of("abc123def456abc123def456abc12345"),
                    Arguments.of("!@#$%^&*()!@#$%^&*()!@#$%^&*()!@"),
                    Arguments.of("abcdefghijklmnopqrstuvwxyz!@#$%")
            );
        }

        private static Stream<Arguments> provideTokenLengths() {
            return Stream.of(
                    Arguments.of(31, false),
                    Arguments.of(32, true),
                    Arguments.of(33, false),
                    Arguments.of(0, false),
                    Arguments.of(100, false)
            );
        }

        private String generateTokenOfLength(int length) {
            String validChars = config.TestConfig.getTokenValidChars();
            StringBuilder token = new StringBuilder();
            for (int i = 0; i < length; i++) {
                char randomChar = validChars.charAt(random.nextInt(validChars.length()));
                token.append(randomChar);
            }
            return token.toString();
        }
    }

    @Nested
    @DisplayName("Интеграционные сценарии")
    class IntegrationTests {

        @ParameterizedTest
        @ValueSource(ints = {400, 401, 403, 404, 500})
        @DisplayName("Ошибки внешнего сервиса при AUTH")
        @Story("Обработка различных ошибок внешних сервисов")
        @Severity(SeverityLevel.NORMAL)
        void externalAuthServiceErrors(int mockStatusCode) {
            MockService.stubAuthEndpoint(mockStatusCode);

            var response = executeLogin(TokenHelper.generateValidToken());
            verifyExternalServiceError(response);
        }
    }
}