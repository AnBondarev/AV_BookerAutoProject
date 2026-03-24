package core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookingIDs {
    private int bookingid;

    @JsonCreator
    public BookingIDs(@JsonProperty("bookingid") int bookingid) {
        this.bookingid = bookingid;
    }

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }
}
