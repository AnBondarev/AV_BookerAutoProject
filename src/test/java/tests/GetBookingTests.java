package tests;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import core.models.BookingDates;
import core.models.BookingIDs;
import core.models.CreatedBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetBookingTests {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreatedBooking createdBooking;
    private Booking newBooking;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        newBooking = new Booking();
        newBooking.setFirstname("Andrey");
        newBooking.setLastname("Bond");
        newBooking.setTotalprice(500);
        newBooking.setDepositpaid(false);
        newBooking.setBookingdates(new BookingDates("2026-04-01", "2026-04-07"));
        newBooking.setAdditionalneeds("Breakfast");
        String requestBody = objectMapper.writeValueAsString(newBooking);
        Response response = apiClient.createBooking(requestBody, 200);
        assertThat(response.getStatusCode()).isEqualTo(200);
        String responseBody = response.asString();
        createdBooking = objectMapper.readValue(responseBody, CreatedBooking.class);
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
        Response response = apiClient.getBookingById(createdBooking.getBookingid(), 200);
        assertThat(response.getStatusCode()).isEqualTo(200);
        String responseBody = response.getBody().asString();
        Booking booking = objectMapper.readValue(responseBody, Booking.class);

        assertThat(booking).isNotNull();
        assertEquals(newBooking.getFirstname(), booking.getFirstname());
        assertEquals(newBooking.getLastname(), booking.getLastname());
        assertEquals(newBooking.getTotalprice(), booking.getTotalprice());
        assertEquals(newBooking.getDepositpaid(), booking.getDepositpaid());
        assertEquals(newBooking.getAdditionalneeds(), booking.getAdditionalneeds());
        assertEquals(newBooking.getBookingdates().getCheckin(), booking.getBookingdates().getCheckin());
        assertEquals(newBooking.getBookingdates().getCheckout(), booking.getBookingdates().getCheckout());

    }

    @AfterEach
    public void tearDown() {
        apiClient.createToken("admin", "password123");
        apiClient.deleteBooking(createdBooking.getBookingid());
        assertThat(apiClient.getBookingById(createdBooking.getBookingid(), 404).getStatusCode()).isEqualTo(404);
    }
}
