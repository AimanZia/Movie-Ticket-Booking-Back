package User.external.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="BOOKING-SERVICE")
public interface BookingService {

    @DeleteMapping("/users/{user_id}")
    ResponseEntity<?> deleteBookingByUser(@PathVariable("user_id") Integer userId);

    @DeleteMapping("")
    ResponseEntity<?> deleteAllUserBookings();
    
}
