package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import core.models.BookingDates;
import core.models.CreatedBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateBookingTests {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    //Храним созданное бронирование
    private CreatedBooking createdBooking;
    //Объект для создания бронирования
    private Booking newBooking;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();

        //Создаем объект Booking и задаем параметры
        newBooking = new Booking();
        newBooking.setFirstname("Andrey");
        newBooking.setLastname("Bond");
        newBooking.setTotalprice(500);
        newBooking.setDepositpaid(false);
        newBooking.setBookingdates(new BookingDates("2026-04-01", "2026-04-07"));
        newBooking.setAdditionalneeds("Breakfast");
    }

    @Test
    public void createBookingTest() throws JsonProcessingException {
        //Переформатирование в строку объекта Booking
        String requestBody = objectMapper.writeValueAsString(newBooking);
        //Выполняем POST-запрос к эндпоинту через APIClient
        Response response = apiClient.createBooking(requestBody, 200);

        //Проверка статус-кода
        assertThat(response.getStatusCode()).isEqualTo(200);

        //Переформатируем тело ответа в строку
        String responseBody = response.asString();
        //Десериализуем тело ответа в объект Booking - objectMapper читает данные из responseBody и сопоставляет с классом CreatedBooking
        createdBooking = objectMapper.readValue(responseBody, CreatedBooking.class);

        //Проверки
        assertThat(createdBooking).isNotNull();
        assertThat(createdBooking.getBookingid()).isPositive();
        assertEquals(newBooking.getFirstname(), createdBooking.getBooking().getFirstname());
        assertEquals(newBooking.getLastname(), createdBooking.getBooking().getLastname());
        assertEquals(newBooking.getTotalprice(), createdBooking.getBooking().getTotalprice());
        assertEquals(newBooking.getDepositpaid(), createdBooking.getBooking().getDepositpaid());
        assertEquals(newBooking.getAdditionalneeds(), createdBooking.getBooking().getAdditionalneeds());
        assertEquals(newBooking.getBookingdates().getCheckin(), createdBooking.getBooking().getBookingdates().getCheckin());
        assertEquals(newBooking.getBookingdates().getCheckout(), createdBooking.getBooking().getBookingdates().getCheckout());
    }

    @AfterEach
    public void tearDown() {
        //Получение токена для DELETE-метода
        apiClient.createToken("admin", "password123");
        //Вызов DELETE-метода для удаления созданного бронирования
        apiClient.deleteBooking(createdBooking.getBookingid());
        //Проверка, что созданный объект действительно удалился
        assertThat(apiClient.getBookingById(createdBooking.getBookingid(), 404).getStatusCode()).isEqualTo(404);
    }
}