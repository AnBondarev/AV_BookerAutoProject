package tests;

import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetBookingTests extends BaseBooking {

    @Test
    @DisplayName("Получение бронирования по id")
    public void testGetBookingById() throws Exception {
        Response response = step("Получение бронирования " + createdBooking.getBookingid(), () ->
                apiClient.getBookingById(createdBooking.getBookingid(), 200)
        );
        assertThat(response.getStatusCode()).as("Не верный код ответа").isEqualTo(200);
        String responseBody = response.getBody().asString();
        Booking booking = objectMapper.readValue(responseBody, Booking.class);

        step("Проверка, что ответ не пустой", () ->
                assertThat(booking).as("Ответ не должен быть пустым").isNotNull()
        );
        step("Проверка поля Firstname", () ->
                assertEquals(newBooking.getFirstname(), booking.getFirstname(),
                        "Значение поля Firstname не совпало с ожидаемым")
        );
        step("Проверка поля Lastname", () ->
                assertEquals(newBooking.getLastname(), booking.getLastname(),
                        "Значение поля Lastname не совпало с ожидаемым")
        );
        step("Проверка поля Totalprice", () ->
                assertEquals(newBooking.getTotalprice(), booking.getTotalprice(),
                        "Значение поля Totalprice не совпало с ожидаемым")
        );
        step("Проверка поля Depositpaid", () ->
                assertEquals(newBooking.getDepositpaid(), booking.getDepositpaid(),
                        "Значение поля Depositpaid не совпало с ожидаемым")
        );
        step("Проверка поля Additionalneeds", () ->
                assertEquals(newBooking.getAdditionalneeds(), booking.getAdditionalneeds(),
                        "Значение поля Additionalneeds не совпало с ожидаемым")
        );
        step("Проверка поля Checkin", () ->
                assertEquals(newBooking.getBookingdates().getCheckin(), booking.getBookingdates().getCheckin(),
                        "Значение поля Checkin не совпало с ожидаемым")
        );
        step("Проверка поля Checkout", () ->
                assertEquals(newBooking.getBookingdates().getCheckout(), booking.getBookingdates().getCheckout(),
                        "Значение поля Checkout не совпало с ожидаемым")
        );
    }
}
