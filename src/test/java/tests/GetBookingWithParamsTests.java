package tests;

import com.fasterxml.jackson.core.type.TypeReference;
import core.models.BookingIDs;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingWithParamsTests extends BaseBooking {

    @Test
    @DisplayName("Получение id бронирования с параметрами firstname & lastname")
    public void testGetBookingWithNameParams() throws Exception {
        Response response = step("Отправка запроса с параметрами: firstname=" + createdBooking.getBooking().getFirstname() + ", lastname=" + createdBooking.getBooking().getLastname(), () ->
                apiClient.getBookingWitchParams(
                        createdBooking.getBooking().getFirstname(),
                        createdBooking.getBooking().getLastname(),
                        null,
                        null,
                        200
                ));
        assertThat(response.getStatusCode()).isEqualTo(200);
        String responseBody = response.getBody().asString();
        List<BookingIDs> bookingIds = objectMapper.readValue(responseBody, new TypeReference<List<BookingIDs>>() {
        });
        assertThat(bookingIds).as("Список id бронирований пуст").isNotEmpty();
        assertThat(bookingIds)
                .as("Нет id, удовлетворяющих условиям фильтра")
                .extracting("bookingid")
                .contains(BookingId);
    }

    @Test
    @DisplayName("Получение бронирования с параметрами checkin & checkout")
    public void testGetBookingWithDatesParams() throws Exception {
        Response response = step("Отправка запроса с параметрами: checkin=" + createdBooking.getBooking().getBookingdates().getCheckin() + ", checkout=" + createdBooking.getBooking().getBookingdates().getCheckout(), () ->
                apiClient.getBookingWitchParams(
                        null,
                        null,
                        createdBooking.getBooking().getBookingdates().getCheckin(),
                        createdBooking.getBooking().getBookingdates().getCheckout(),
                        200
                ));
        assertThat(response.getStatusCode()).isEqualTo(200);
        String responseBody = response.getBody().asString();
        List<BookingIDs> bookingIds = objectMapper.readValue(responseBody, new TypeReference<List<BookingIDs>>() {
        });
        assertThat(bookingIds).as("Список id бронирований пуст").isNotEmpty();
        assertThat(bookingIds)
                .as("Нет id, удовлетворяющих условиям фильтра")
                .extracting("bookingid")
                .contains(BookingId);
    }
}
