package Booking.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import Booking.entities.Booking;
import Booking.entities.BookingRequest;
import Booking.entities.Show;
import Booking.repositories.BookingRepository;
import Booking.repositories.ShowRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired 
    private ShowRepository showRepository;

    @Autowired 
    private RestTemplate restTemplate;

    @Value("${booking.wallet.url}")
    private String bookingWalletUrl;

    public void createBooking(BookingRequest bookingRequest) {
        Booking booking=new Booking();
        booking.setSeatsBooked(bookingRequest.getSeatsBooked());
        booking.setShow(this.getShowById(bookingRequest.getShowId()));
        booking.setUserId(bookingRequest.getUserId());
        bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUser(Integer userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getBookingsByUserAndShow(Integer userId, Integer showId) {
        return bookingRepository.findByUserIdAndShowId(userId, showId);
    }

    public void deleteAllBookings() {
        List<Booking> allBookings = this.bookingRepository.findAll();
        List<Map<Integer, Integer>> listUsers = new ArrayList<>();

        allBookings.forEach(booking ->{
            Show show = booking.getShow();
            show.setSeatsAvailable(show.getSeatsAvailable()+booking.getSeatsBooked());
            Map<Integer,Integer> userMap = new HashMap<Integer,Integer>();
            userMap.put(booking.getId(), booking.getSeatsBooked()*show.getPrice());
            listUsers.add(userMap);
        });
        updateAllUsersWallet(listUsers);
        bookingRepository.deleteAll();
    }

    public Show getShowById(Integer showId) {
        return showRepository.findById(showId).orElse(null);
    }

    public Integer deleteBookingsByUser(Integer userId) {
        List<Booking> bookings = this.bookingRepository.findByUserId(userId);
        if(bookings.isEmpty() || bookings.equals(null)){
            return null;
        }
        final Integer[] totalAmount = {0};
        bookings.forEach( booking -> {
            Show show = booking.getShow();
            Integer amount = booking.getSeatsBooked() * show.getPrice();
            totalAmount[0] += amount;
            show.setSeatsAvailable(show.getSeatsAvailable()+booking.getSeatsBooked());
            this.showRepository.save(show);
        });
        this.bookingRepository.deleteByUserId(userId);
        return totalAmount[0];
    }

    public Integer deleteBookingsByUserAndShow(Integer userId, Integer showId) {
        List<Booking> bookings = this.bookingRepository.findByUserIdAndShowId(userId, showId);
        if(bookings.isEmpty() || bookings.equals(null)){
            return null;
        }
        final Integer[] totalAmount = {0};
        bookings.forEach( booking -> {
            Show show = booking.getShow();
            Integer amount = booking.getSeatsBooked() * show.getPrice();
            totalAmount[0] += amount;
            show.setSeatsAvailable(show.getSeatsAvailable()+booking.getSeatsBooked());
            this.showRepository.save(show);
            this.bookingRepository.deleteByUserIdAndShowId(booking.getUserId(), showId);
        });
        
        return totalAmount[0];
    }

    public List<Booking> findBookingByUser(Integer userId) {
        return this.bookingRepository.findByUserId(userId);
    }

    @Async
    private void updateAllUsersWallet(List<Map<Integer, Integer>> listUsers) {
        listUsers.forEach(user->{
             for (Map.Entry<Integer,Integer> entry : user.entrySet()) {
                updateBalance(entry.getValue(), entry.getKey(), "credit");
             }
        });
    }

    private String updateBalance(Integer bookingCost,Integer userId,String action){  
        String url = bookingWalletUrl+userId;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            String amount = String.valueOf(bookingCost);
            String requestBody = String.format("{\"action\": \"%s\", \"amount\": \"%s\"}", action, amount);
            HttpEntity<String> entity = new HttpEntity<>(requestBody,headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    String.class
            );

            return responseEntity.getStatusCode().toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
