package tests;

import com.fasterxml.jackson.core.type.TypeReference;
import core.models.BookingIDs;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBookingTests extends BaseBooking {

    @Test
    public void testDeleteBooking() throws Exception {

        //Получить список всех id бронирований
        Response responseIds = apiClient.getBooking();
        String responseBody = responseIds.getBody().asString();
        List<BookingIDs> bookingIds = objectMapper.readValue(responseBody, new TypeReference<List<BookingIDs>>() {
        });

        if (bookingIds != null && !bookingIds.isEmpty()) {
            //Выбрать один id из списка полученных
            int Id = bookingIds.get(5).getBookingid();

            //Удалить полученный Id
            apiClient.createToken("admin", "password123");
            Response responseDelete = apiClient.deleteBooking(Id);
            assertThat(responseDelete.getStatusCode()).isEqualTo(201);

            //Проверка, что этого id не существует
            Response responseGet = apiClient.getBookingById(Id, 404);
            assertThat(responseGet.getStatusCode()).isEqualTo(404);
        } else throw new RuntimeException("Список бронирований пуст");
    }

}
