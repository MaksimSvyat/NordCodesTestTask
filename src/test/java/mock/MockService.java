package mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import config.TestConfig;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class MockService {
    private static WireMockServer wireMockServer;
    private static final int PORT = TestConfig.getMockPort();
    private static final String AUTH_ENDPOINT = TestConfig.getMockAuthEndpoint();
    private static final String ACTION_ENDPOINT = TestConfig.getMockActionEndpoint();

    public static void start() {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(wireMockConfig().port(PORT));
            wireMockServer.start();
            WireMock.configureFor("localhost", PORT);
        }
    }

    public static void stubAuthEndpoint(int statusCode) {
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(AUTH_ENDPOINT))
                .willReturn(WireMock.aResponse().withStatus(statusCode)));
    }

    public static void stubDoActionEndpoint(int statusCode) {
        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo(ACTION_ENDPOINT))
                .willReturn(WireMock.aResponse().withStatus(statusCode)));
    }
}
