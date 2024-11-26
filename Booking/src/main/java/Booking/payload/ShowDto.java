package Booking.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowDto {
    private Integer id;
    @JsonProperty("theatre_id")
    private Integer theatreId;
    private String title;
    private Integer price;
    @JsonProperty("seats_available")
    private Integer seatsAvailable;
}
