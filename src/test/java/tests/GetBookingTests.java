package tests;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import core.models.BookingIDs;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingTests {
    private APIClient apiClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetBooking() throws Exception {
        Response response = apiClient.getBooking();

        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        List<BookingIDs> bookingIds = objectMapper.readValue(responseBody, new TypeReference<List<BookingIDs>>() {
        });

        assertThat(bookingIds).isNotEmpty();

        for (BookingIDs booking : bookingIds) {
            assertThat(booking.getBookingid()).isGreaterThan(0);
        }
    }

    @Test
    public void testGetBookingById() throws Exception {
        Response response = apiClient.getBookingById(2, 200);

        assertThat(response.getStatusCode()).isEqualTo(200);

        String responseBody = response.getBody().asString();
        Booking booking = objectMapper.readValue(responseBody, Booking.class);

        assertThat(booking.getFirstname()).isNotBlank();
        assertThat(booking.getLastname()).isNotBlank();
        assertThat(booking.getTotalprice()).isGreaterThan(0);
        //assertThat(booking.getDepositpaid()).isNotNull();
        assertThat(booking.getBookingdates()).isNotNull();
        assertThat(booking.getBookingdates().getCheckin()).isNotBlank();
        assertThat(booking.getBookingdates().getCheckout()).isNotBlank();
        assertThat(booking.getAdditionalneeds()).isNotBlank();

    }

}
