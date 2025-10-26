package helpers;

import com.github.javafaker.Faker;
import config.TestConfig;

public class TokenHelper {
    private static final Faker faker = new Faker();
    private static final String VALID_CHARS = TestConfig.getTokenValidChars();
    private static final int TOKEN_LENGTH = TestConfig.getTokenLength();

    public static String generateValidToken() {
        return generateToken(VALID_CHARS);
    }

    public static String generateInvalidToken() {
        String invalidChars = "abcdefghijklmnopqrstuvwxyz!@#$%^&*()_+-=[]{}|;:,.<>?";
        return generateToken(invalidChars);
    }

    private static String generateToken(String validChars) {
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < TokenHelper.TOKEN_LENGTH; i++) {
            char randomChar = validChars.charAt(faker.random().nextInt(validChars.length()));
            token.append(randomChar);
        }
        return token.toString();
    }
}
