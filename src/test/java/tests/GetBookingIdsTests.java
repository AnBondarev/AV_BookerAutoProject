package tests;


import com.fasterxml.jackson.core.type.TypeReference;
import core.models.BookingIDs;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingIdsTests extends BaseBooking {

    @Test
    @DisplayName("Проверка получения списка id бронирований")
    public void testGetBookingIds() throws Exception {
        Response response = apiClient.getBooking();
        step("Получение списка id", () ->
                assertThat(response.getStatusCode()).isEqualTo(200)
        );
        String responseBody = response.getBody().asString();
        List<BookingIDs> bookingIds = objectMapper.readValue(responseBody, new TypeReference<List<BookingIDs>>() {
        });

        step("Проверка списка id", () ->
                assertThat(bookingIds).as("Пришел пустой список").isNotEmpty()
        );
        for (BookingIDs booking : bookingIds) {
            step("Проверка списка id", () ->
                    assertThat(booking.getBookingid()).as("Значения id невалидны").isGreaterThan(0)
            );
        }
    }
}
