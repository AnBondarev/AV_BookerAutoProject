package tests;

import com.fasterxml.jackson.core.type.TypeReference;
import core.models.BookingIDs;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingWithParamsTests extends BaseBooking {

    @Test
    public void testGetBookingWithNameParams() throws Exception {
        Response response = apiClient.getBookingWitchParams(
                createdBooking.getBooking().getFirstname(),
                createdBooking.getBooking().getLastname(),
                null,
                null,
                200
        );
        assertThat(response.getStatusCode()).isEqualTo(200);
        String responseBody = response.getBody().asString();
        List<BookingIDs> bookingIds = objectMapper.readValue(responseBody, new TypeReference<List<BookingIDs>>() {
        });
        assertThat(bookingIds).isNotEmpty();
        assertThat(bookingIds)
                .extracting("bookingid")
                .contains(BookingId);
    }

    @Test
    public void testGetBookingWithDatesParams() throws Exception {
        Response response = apiClient.getBookingWitchParams(
                null,
                null,
                createdBooking.getBooking().getBookingdates().getCheckin(),
                createdBooking.getBooking().getBookingdates().getCheckout(),
                200
        );
        assertThat(response.getStatusCode()).isEqualTo(200);
        String responseBody = response.getBody().asString();
        List<BookingIDs> bookingIds = objectMapper.readValue(responseBody, new TypeReference<List<BookingIDs>>() {
        });
        assertThat(bookingIds).isNotEmpty();
        assertThat(bookingIds)
                .extracting("bookingid")
                .contains(BookingId);
    }
}
