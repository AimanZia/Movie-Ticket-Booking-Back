package Booking.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Booking {
    
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator= "bookingidgen")
    @SequenceGenerator(name= "bookingidgen", initialValue = 1, allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    private Integer userId;

    @ManyToOne
    private Show show;

    private Integer seatsBooked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Integer getSeatsBooked() {
        return seatsBooked;
    }

    public void setSeatsBooked(Integer seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    @Override
    public String toString() {
        return "Booking [id=" + id + ", userId=" + userId + ", show=" + show + ", seatsBooked=" + seatsBooked + "]";
    }
    
    
}
