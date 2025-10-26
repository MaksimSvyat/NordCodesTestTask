package tests;

import mock.MockService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    @BeforeAll
    static void setUpAll() {
        MockService.start();
    }

    @BeforeEach
    void setUp() {
        MockService.stubAuthEndpoint(200);
        MockService.stubDoActionEndpoint(200);
    }
}