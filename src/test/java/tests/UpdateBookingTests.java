package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.models.Booking;
import core.models.BookingDates;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateBookingTests extends BaseBooking {
    private Booking updatedBooking; //Храним измененное бронирование
    private Booking updateBooking; //Объект для изменения бронирования

    @Test
    @DisplayName("Проверка изменения существующего бронирования")
    public void testUpdateBooking() throws JsonProcessingException {
        apiClient.createToken("admin", "password123");

        updateBooking = new Booking();
        updateBooking.setFirstname("Viktor"); //Изменен
        updateBooking.setLastname("Bond");
        updateBooking.setTotalprice(550); //Изменен
        updateBooking.setDepositpaid(false);
        updateBooking.setBookingdates(new BookingDates("2026-04-01", "2026-04-07"));
        updateBooking.setAdditionalneeds("Breakfast");

        String requestBody = objectMapper.writeValueAsString(updateBooking);
        //Выполняем PUT-запрос к эндпоинту через APIClient
        Response response = step("Отправка запроса на изменение бронирования id: " + BookingId, () ->
                apiClient.updateBooking(requestBody, BookingId)
        );
        //Проверка статус-кода
        assertThat(response.getStatusCode()).as("Сервер вернул не верный ответ").isEqualTo(200);
        //Переформатируем тело ответа в строку
        String responseBody = response.asString();
        //Десериализуем тело ответа в объект Booking - objectMapper читает данные из responseBody и сопоставляет с классом Booking
        updatedBooking = objectMapper.readValue(responseBody, Booking.class);

        //Проверки
        step("Проверка, что ответ не пустой", () ->
                assertThat(updatedBooking).as("Ответ не должен быть пустым").isNotNull()
        );
        step("Проверка поля Firstname", () ->
                assertEquals(updateBooking.getFirstname(), updatedBooking.getFirstname(),
                        "Значение поля Firstname не совпало с ожидаемым")
        );
        step("Проверка поля Lastname", () ->
                assertEquals(newBooking.getLastname(), updatedBooking.getLastname(),
                        "Значение поля Lastname не совпало с ожидаемым")
        );
        step("Проверка поля Totalprice", () ->
                assertEquals(updateBooking.getTotalprice(), updatedBooking.getTotalprice(),
                        "Значение поля Totalprice не совпало с ожидаемым")
        );
        step("Проверка поля Depositpaid", () ->
                assertEquals(newBooking.getDepositpaid(), updatedBooking.getDepositpaid(),
                        "Значение поля Depositpaid не совпало с ожидаемым")
        );
        step("Проверка поля Additionalneeds", () ->
                assertEquals(newBooking.getAdditionalneeds(), updatedBooking.getAdditionalneeds(),
                        "Значение поля Additionalneeds не совпало с ожидаемым")
        );
        step("Проверка поля Checkin", () ->
                assertEquals(newBooking.getBookingdates().getCheckin(), updatedBooking.getBookingdates().getCheckin(),
                        "Значение поля Checkin не совпало с ожидаемым")
        );
        step("Проверка поля Checkout", () ->
                assertEquals(newBooking.getBookingdates().getCheckout(), updatedBooking.getBookingdates().getCheckout(),
                        "Значение поля Checkout не совпало с ожидаемым")
        );
    }
}
