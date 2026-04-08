package tests;


import com.fasterxml.jackson.core.type.TypeReference;
import core.models.BookingIDs;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingIdsTests extends BaseBooking {

    @Test
    public void testGetBookingIds() throws Exception {
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
}
