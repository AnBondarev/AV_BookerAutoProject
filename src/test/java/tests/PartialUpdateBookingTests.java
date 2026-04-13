package tests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PartialUpdateBookingTests extends BaseBooking {
    private Booking updatedBooking; //Храним измененное бронирование
    private Booking updateBooking; //Объект для изменения бронирования
    private ObjectMapper patchMapper; //Маппер

    @Test
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
        Response response = apiClient.partialUpdateBooking(requestBody, BookingId);
        //Проверка статус-кода
        assertThat(response.getStatusCode()).isEqualTo(200);
        //Переформатируем тело ответа в строку
        String responseBody = response.asString();
        //Десериализуем тело ответа в объект Booking - objectMapper читает данные из responseBody и сопоставляет с классом Booking
        updatedBooking = objectMapper.readValue(responseBody, Booking.class);

        //Проверки
        assertThat(updatedBooking).isNotNull();
        //Измененные поля должны быть равны отправленным
        assertEquals(updateBooking.getFirstname(), updatedBooking.getFirstname());
        assertEquals(updateBooking.getTotalprice(), updatedBooking.getTotalprice());
        //Неизменные поля должны остаться, как в исходном бронировании
        assertEquals(newBooking.getLastname(), updatedBooking.getLastname());
        assertEquals(newBooking.getDepositpaid(), updatedBooking.getDepositpaid());
        assertEquals(newBooking.getAdditionalneeds(), updatedBooking.getAdditionalneeds());
        assertEquals(newBooking.getBookingdates().getCheckin(), updatedBooking.getBookingdates().getCheckin());
        assertEquals(newBooking.getBookingdates().getCheckout(), updatedBooking.getBookingdates().getCheckout());
    }
}
