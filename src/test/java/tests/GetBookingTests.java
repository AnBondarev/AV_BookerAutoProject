package tests;

import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetBookingTests extends BaseBooking {

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
}
