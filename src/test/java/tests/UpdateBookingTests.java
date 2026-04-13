package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.models.Booking;
import core.models.BookingDates;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateBookingTests extends BaseBooking {
    private Booking updatedBooking; //Храним измененное бронирование
    private Booking updateBooking; //Объект для изменения бронирования

    @Test
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
        Response response = apiClient.updateBooking(requestBody, BookingId);
        //Проверка статус-кода
        assertThat(response.getStatusCode()).isEqualTo(200);
        //Переформатируем тело ответа в строку
        String responseBody = response.asString();
        //Десериализуем тело ответа в объект Booking - objectMapper читает данные из responseBody и сопоставляет с классом Booking
        updatedBooking = objectMapper.readValue(responseBody, Booking.class);

        //Проверки
        assertThat(updatedBooking).isNotNull();
        assertEquals(updateBooking.getFirstname(), updatedBooking.getFirstname());
        assertEquals(newBooking.getLastname(), updatedBooking.getLastname());
        assertEquals(updateBooking.getTotalprice(), updatedBooking.getTotalprice());
        assertEquals(newBooking.getDepositpaid(), updatedBooking.getDepositpaid());
        assertEquals(newBooking.getAdditionalneeds(), updatedBooking.getAdditionalneeds());
        assertEquals(newBooking.getBookingdates().getCheckin(), updatedBooking.getBookingdates().getCheckin());
        assertEquals(newBooking.getBookingdates().getCheckout(), updatedBooking.getBookingdates().getCheckout());
    }
}
