package clients;

import config.TestConfig;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApplicationClient {
    private static final String BASE_URL = TestConfig.getBaseUrl();
    private static final String API_KEY = TestConfig.getApiKey();
    private static final String ENDPOINT = TestConfig.getEndpoint();

    static {
        RestAssured.baseURI = BASE_URL;
    }

    public Response sendRequest(String token, String action) {
        try {
            Map<String, String> formParams = new HashMap<>();
            formParams.put("token", token);
            formParams.put("action", action);

            return given()
                    .header("X-Api-Key", API_KEY)
                    .contentType("application/x-www-form-urlencoded")
                    .accept("application/json")
                    .formParams(formParams)
                    .post(ENDPOINT);
        } catch (Exception e) {
            String errorDetails = String.format(
                    "Request failed: %s\nToken: %s\nAction: %s\nTime: %s",
                    e.getMessage(), token, action, LocalDateTime.now()
            );
            Allure.addAttachment("Request Failed", "text/plain", errorDetails);
            throw e;
        }
    }

    public Response sendRequestWithoutApiKey(String token, String action) {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("token", token);
        formParams.put("action", action);

        return given()
                .contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .formParams(formParams)
                .post(ENDPOINT);
    }
}
