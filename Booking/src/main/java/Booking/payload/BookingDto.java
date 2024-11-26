package Booking.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDto {
    private Integer id;
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("show_id")
    private Integer showId;
    @JsonProperty("seats_booked")
    private Integer seatsBooked;
}
