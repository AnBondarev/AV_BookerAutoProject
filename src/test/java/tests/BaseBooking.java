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

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class BaseBooking {
    APIClient apiClient;
    ObjectMapper objectMapper;
    CreatedBooking createdBooking; //Храним созданное бронирование
    Booking newBooking; //Объект для создания бронирования
    int BookingId;
    //Модификатор доступа - default. Видимость в пределах пакета tests

    @BeforeEach
    public void createNewBooking() throws JsonProcessingException {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        newBooking = new Booking(); //Создаем объект Booking и задаем параметры
        newBooking.setFirstname("Andrey");
        newBooking.setLastname("Bond");
        newBooking.setTotalprice(500);
        newBooking.setDepositpaid(false);
        newBooking.setBookingdates(new BookingDates("2026-04-01", "2026-04-07"));
        newBooking.setAdditionalneeds("Breakfast");

        //Переформатирование в строку объекта Booking
        String requestBody = objectMapper.writeValueAsString(newBooking);
        //Выполняем POST-запрос к эндпоинту через APIClient
        Response response = step("Создание тестового бронирования", () ->
                apiClient.createBooking(requestBody, 200)
        );
        //Переформатируем тело ответа в строку
        String responseBody = response.asString();
        //Десериализуем тело ответа в объект Booking - objectMapper читает данные из responseBody и сопоставляет с классом CreatedBooking
        createdBooking = objectMapper.readValue(responseBody, CreatedBooking.class);
        BookingId = step("Бронированию присвоен id: " + createdBooking.getBookingid(), () ->
                createdBooking.getBookingid());
    }

    @AfterEach
    public void tearDown() {
        //Получение токена для DELETE-метода
        apiClient.createToken("admin", "password123");
        //Вызов DELETE-метода для удаления созданного бронирования
        apiClient.deleteBooking(createdBooking.getBookingid());
        //Проверка, что созданный объект действительно удалился
        step("Удаление бронирования: проверка удаления бронирования id: " + createdBooking.getBookingid(), () ->
                assertThat(apiClient.getBookingById(createdBooking.getBookingid(), 404).getStatusCode()).isEqualTo(404)
        );
    }
}
