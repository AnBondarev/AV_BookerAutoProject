package tests;

import core.clients.APIClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class HealthCheckTests {
    private APIClient apiClient;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
    }

    @Test
    @DisplayName("Проверка доступности сервера")
    public void testPing(){
        Response response = apiClient.ping();
        step("Проверка доступности сервера", () ->
                assertThat(response.getStatusCode()).as("Сервер не отвечает").isEqualTo(201)
        );
    }
}
