package steps;

import builders.RequestData;
import builders.RequestDataBuilder;
import clients.ApplicationClient;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;

public class ApplicationSteps {
    private static final ApplicationClient client = new ApplicationClient();

    @Step("Выполнение запроса с данными: {requestData}")
    public static Response executeRequest(RequestData requestData) {
        attachRequestDetails(requestData);

        Response response;
        if (requestData.isWithApiKey()) {
            response = client.sendRequest(requestData.getToken(), requestData.getAction());
        } else {
            response = client.sendRequestWithoutApiKey(requestData.getToken(), requestData.getAction());
        }

        attachResponseDetails(response);
        return response;
    }

    @Step("Выполнение LOGIN")
    public static Response executeLogin(String token) {
        var requestData = RequestDataBuilder.create()
                .withToken(token)
                .withLoginAction()
                .build();
        return executeRequest(requestData);
    }

    @Step("Выполнение ACTION")
    public static Response executeAction(String token) {
        var requestData = RequestDataBuilder.create()
                .withToken(token)
                .withActionAction()
                .build();
        return executeRequest(requestData);
    }

    @Step("Выполнение LOGOUT")
    public static Response executeLogout(String token) {
        var requestData = RequestDataBuilder.create()
                .withToken(token)
                .withLogoutAction()
                .build();
        return executeRequest(requestData);
    }

    @Step("Выполнение полного цикла: LOGIN → ACTION → LOGOUT")
    public static void executeFullFlow(String token) {
        Response loginResponse = executeLogin(token);
        verifySuccessResponse(loginResponse);

        Response actionResponse = executeAction(token);
        verifySuccessResponse(actionResponse);

        Response logoutResponse = executeLogout(token);
        verifySuccessResponse(logoutResponse);
    }

    @Step("Проверка успешного ответа")
    public static void verifySuccessResponse(Response response) {
        response.then()
                .statusCode(200)
                .body("result", equalTo("OK"))
                .body("message", nullValue());
    }

    @Step("Проверка ошибки валидации")
    public static void verifyValidationError(Response response) {
        response.then()
                .statusCode(400)
                .body("result", equalTo("ERROR"))
                .body("message", not(emptyOrNullString()));
    }

    @Step("Проверка ошибки авторизации")
    public static void verifyAuthError(Response response) {
        response.then()
                .statusCode(401)
                .body("result", equalTo("ERROR"))
                .body("message", not(emptyOrNullString()));
    }

    @Step("Проверка ошибки доступа")
    public static void verifyForbiddenError(Response response) {
        response.then()
                .statusCode(403)
                .body("result", equalTo("ERROR"))
                .body("message", not(emptyOrNullString()));
    }

    @Step("Проверка ошибки внешнего сервиса (ожидается 500)")
    public static void verifyExternalServiceError(Response response) {
        response.then()
                .statusCode(500)
                .body("result", equalTo("ERROR"))
                .body("message", not(emptyOrNullString()));
    }

    // Вспомогательные методы для аттачментов
    private static void attachRequestDetails(RequestData requestData) {
        String requestDetails = String.format(
                "Token: %s\nAction: %s\nAPI Key: %s\nTime: %s",
                requestData.getToken(),
                requestData.getAction(),
                requestData.isWithApiKey() ? "present" : "missing",
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
        Allure.addAttachment("Request Details", "text/plain", requestDetails);
    }

    private static void attachResponseDetails(Response response) {
        String responseDetails = String.format(
                "Status Code: %d\nResponse Body: %s\nTime: %s",
                response.getStatusCode(),
                response.getBody().asString(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
        Allure.addAttachment("Response Details", "text/plain", responseDetails);
    }
}
