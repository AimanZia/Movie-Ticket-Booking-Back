package Booking.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookingRequest {

    @JsonProperty("show_id")
    private Integer showId;
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("seats_booked")
    private Integer seatsBooked;

    public Integer getShowId() {
        return showId;
    }
    public void setShowId(Integer showId) {
        this.showId = showId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getSeatsBooked() {
        return seatsBooked;
    }
    public void setSeatsBooked(Integer seatsBooked) {
        this.seatsBooked = seatsBooked;
    }
    @Override
    public String toString() {
        return "BookingRequest [showId=" + showId + ", userId=" + userId + ", seatsBooked=" + seatsBooked + "]";
    }

}
