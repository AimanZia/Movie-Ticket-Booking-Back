package Booking.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import Booking.entities.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer>{
    List<Booking> findByUserId(Integer userId);
    List<Booking> findByUserIdAndShowId(Integer userId, Integer showId);
    void deleteByUserId(Integer userId);
    void deleteByUserIdAndShowId(Integer userId,Integer showId);
}