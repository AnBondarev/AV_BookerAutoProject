package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateBookingTests extends BaseBooking {

    @Test
    @DisplayName("Проверка создания нового бронирования")
    public void createBookingTest() throws JsonProcessingException {
        //Проверки
        //Для AssertJ - рекомендуемый стиль. step принимает 2 аргумента
        step("Проверка, что ответ не пустой", () ->
                assertThat(createdBooking).as("Ответ не должен быть пустым").isNotNull()
        );
        step("Проверка поля id", () ->
                assertThat(createdBooking.getBookingid()).as("Значения поля id невалидны").isPositive()
        );
        //Для JUnit - принимает 3 параметра assertEquals(expected, actual, message)
        step("Проверка поля Firstname", () ->
                assertEquals(newBooking.getFirstname(), createdBooking.getBooking().getFirstname(),
                        "Значение поля Firstname не совпало с ожидаемым")
        );
        step("Проверка поля Lastname", () ->
                assertEquals(newBooking.getLastname(), createdBooking.getBooking().getLastname(),
                        "Значение поля Lastname не совпало с ожидаемым")
        );
        step("Проверка поля Totalprice", () ->
                assertEquals(newBooking.getTotalprice(), createdBooking.getBooking().getTotalprice(),
                        "Значение поля Totalprice не совпало с ожидаемым")
        );
        step("Проверка поля Depositpaid", () ->
                assertEquals(newBooking.getDepositpaid(), createdBooking.getBooking().getDepositpaid(),
                        "Значение поля Depositpaid не совпало с ожидаемым")
        );
        step("Проверка поля Additionalneeds", () ->
                assertEquals(newBooking.getAdditionalneeds(), createdBooking.getBooking().getAdditionalneeds(),
                        "Значение поля Additionalneeds не совпало с ожидаемым")
        );
        step("Проверка поля Checkin", () ->
                assertEquals(newBooking.getBookingdates().getCheckin(), createdBooking.getBooking().getBookingdates().getCheckin(),
                        "Значение поля Checkin не совпало с ожидаемым")
        );
        step("Проверка поля Checkout", () ->
                assertEquals(newBooking.getBookingdates().getCheckout(), createdBooking.getBooking().getBookingdates().getCheckout(),
                        "Значение поля Checkout не совпало с ожидаемым")
        );
    }
}