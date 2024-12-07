package Booking.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Booking.entities.Show;

public interface ShowRepository extends JpaRepository<Show,Integer>{
    List<Show> findByTheatreId(Integer theatreId);
}
