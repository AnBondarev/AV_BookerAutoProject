package tests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PartialUpdateBookingTests extends BaseBooking {
    private Booking updatedBooking; //Храним измененное бронирование
    private Booking updateBooking; //Объект для изменения бронирования
    private ObjectMapper patchMapper; //Маппер

    @Test
    @DisplayName("Проверка частичного изменения существующего бронирования")
    public void testPartialUpdateBooking() throws JsonProcessingException {
        apiClient.createToken("admin", "password123");

        updateBooking = new Booking();
        updateBooking.setFirstname("Viktor"); //Изменен
        updateBooking.setTotalprice(550); //Изменен

        //Игнорирование null-параметров в JSON
        patchMapper = new ObjectMapper();
        patchMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String requestBody = patchMapper.writeValueAsString(updateBooking);
        //Выполняем PATCH-запрос к эндпоинту через APIClient
        Response response = step("Отправка запроса на частичное изменение бронирования id: " + BookingId, () ->
                apiClient.partialUpdateBooking(requestBody, BookingId)
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
        //Измененные поля должны быть равны отправленным
        step("Проверка поля Firstname", () ->
                assertEquals(updateBooking.getFirstname(), updatedBooking.getFirstname(),
                        "Значение поля Firstname не совпало с ожидаемым")
        );
        step("Проверка поля Totalprice", () ->
                assertEquals(updateBooking.getTotalprice(), updatedBooking.getTotalprice(),
                        "Значение поля Totalprice не совпало с ожидаемым")
        );
        //Неизменные поля должны остаться, как в исходном бронировании
        step("Проверка поля Lastname", () ->
                assertEquals(newBooking.getLastname(), updatedBooking.getLastname(),
                        "Значение поля Lastname не совпало с ожидаемым")
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
