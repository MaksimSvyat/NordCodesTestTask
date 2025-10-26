package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class TestConfig {
    private static final Properties properties = new Properties();
    private static final Logger logger = Logger.getLogger(TestConfig.class.getName());

    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
    private static final String DEFAULT_API_KEY = "qazWSXedc";
    private static final String DEFAULT_ENDPOINT = "/endpoint";
    private static final int DEFAULT_MOCK_PORT = 8888;
    private static final String DEFAULT_MOCK_AUTH_ENDPOINT = "/auth";
    private static final String DEFAULT_MOCK_ACTION_ENDPOINT = "/doAction";
    private static final String DEFAULT_TOKEN_CHARS = "ABCDEF0123456789";
    private static final int DEFAULT_TOKEN_LENGTH = 32;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.warning("Config file not found, using default values");
                return;
            }
            properties.load(input);
            logger.info("Configuration loaded successfully");
        } catch (IOException e) {
            logger.severe("Error loading config: " + e.getMessage());
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("app.base.url", DEFAULT_BASE_URL);
    }

    public static String getApiKey() {
        return properties.getProperty("app.api.key", DEFAULT_API_KEY);
    }

    public static String getEndpoint() {
        return properties.getProperty("app.endpoint", DEFAULT_ENDPOINT);
    }

    public static int getMockPort() {
        try {
            return Integer.parseInt(properties.getProperty("mock.port", String.valueOf(DEFAULT_MOCK_PORT)));
        } catch (NumberFormatException e) {
            logger.warning("Invalid mock.port, using default: " + DEFAULT_MOCK_PORT);
            return DEFAULT_MOCK_PORT;
        }
    }

    public static String getMockAuthEndpoint() {
        return properties.getProperty("mock.auth.endpoint", DEFAULT_MOCK_AUTH_ENDPOINT);
    }

    public static String getMockActionEndpoint() {
        return properties.getProperty("mock.action.endpoint", DEFAULT_MOCK_ACTION_ENDPOINT);
    }

    public static String getTokenValidChars() {
        return properties.getProperty("token.valid.chars", DEFAULT_TOKEN_CHARS);
    }

    public static int getTokenLength() {
        try {
            return Integer.parseInt(properties.getProperty("test.token.length", String.valueOf(DEFAULT_TOKEN_LENGTH)));
        } catch (NumberFormatException e) {
            logger.warning("Invalid test.token.length, using default: " + DEFAULT_TOKEN_LENGTH);
            return DEFAULT_TOKEN_LENGTH;
        }
    }
}
