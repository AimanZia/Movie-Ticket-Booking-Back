package Booking.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import Booking.entities.Booking;
import Booking.entities.BookingRequest;
import Booking.entities.Show;
import Booking.payload.BookingDto;
import Booking.service.BookingService;
import Booking.service.ShowService;
import jakarta.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = {"http://localhost:8080/users","http://localhost:8082/wallets"})
public class BookingController {

    @Autowired 
    BookingService bookingService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired 
    private ShowService showService;

    @Value("${booking.user.url}")
    private String bookingUserUrl;

    @Value("${booking.wallet.url}")
    private String bookingWalletUrl;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {

        Show show = bookingService.getShowById(bookingRequest.getShowId());
        if (show == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Show not found.");
        }
        if (show.getSeatsAvailable() < bookingRequest.getSeatsBooked()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough seats available.");
        }
        String userStatus = checkUserExists(bookingRequest.getUserId());
        if(userStatus==null || !(userStatus).equals("200 OK")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Does not Exists");
        }
        
        Double walletAmount = Double.parseDouble(checkForWalletDetails(bookingRequest.getUserId()));
        Integer bookingCost = show.getPrice()*bookingRequest.getSeatsBooked();
        System.out.println("Wallet Details"+walletAmount);
        if((walletAmount - bookingCost) < 0 ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient Balanace");
        }
        String updateBalanceCode = updateBalance(bookingCost,bookingRequest.getUserId(),"debit");
        if(!updateBalanceCode.equals("200 OK")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wallet Error");
        }
        bookingService.createBooking(bookingRequest);
        show.setSeatsAvailable(show.getSeatsAvailable() - bookingRequest.getSeatsBooked());
        // Update show with reduced available seats
        // Save the updated show
        this.showService.saveShow(show);

        return ResponseEntity.ok("Booking created successfully.");
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<BookingDto>> getBookingByUser(@PathVariable Integer userId) {
        List<Booking> bookingByUser = this.bookingService.findBookingByUser(userId);
        List<BookingDto> bookingByUserDto = bookingByUser.stream().map(this::bookingtoBookingDto).collect(Collectors.toList());
        return ResponseEntity.ok(bookingByUserDto);
    }

    @DeleteMapping(value = MediaType.APPLICATION_JSON)
    public ResponseEntity<Map<String,String>> deleteAllBookings() {
        bookingService.deleteAllBookings();
        return ResponseEntity.ok(Map.of("Message","All bookings deleted successfully."));
    }

    @DeleteMapping(value = "/users/{user_id}",produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<?> deleteBookingsByUser(@PathVariable("user_id") Integer userId) {
        Integer amountToCredit = bookingService.deleteBookingsByUser(userId);
        if(amountToCredit==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Wallet Error"));
        }
        updateBalance(amountToCredit, userId,"credit");
        return ResponseEntity.ok(Map.of("message","Bookings for user " + userId + " deleted successfully."));
    }

    @DeleteMapping(value="/users/{user_id}/shows/{show_id}",produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<?> deleteBookingsByUserAndShow(@PathVariable("user_id") Integer userId, @PathVariable("show_id") Integer showId) {
        Integer amountToCredit = bookingService.deleteBookingsByUserAndShow(userId, showId);
        if(amountToCredit==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wallet Error");
        }
        updateBalance(amountToCredit, userId,"credit");
        return ResponseEntity.ok(Map.of("message","Bookings for user " + userId + " and show " + showId + " deleted successfully."));
    }
    
    private String checkUserExists(Integer userId){
        String url = bookingUserUrl+userId;

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            System.out.println("Status code: " + responseEntity.getStatusCode());
            System.out.println("Response body: " + responseEntity.getBody());

            return responseEntity.getStatusCode().toString();
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private String checkForWalletDetails(Integer userId){
        String url = bookingWalletUrl+userId;

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            ObjectMapper objectMapper = new ObjectMapper();
            
            Map<String, Object> responseBodyMap = objectMapper.readValue(responseEntity.getBody(), new TypeReference<Map<String, Object>>() {});

            // Get the value associated with the specific key
            Object balance = responseBodyMap.get("balance");

            return balance.toString();
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
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

    private BookingDto bookingtoBookingDto(Booking booking){
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setShowId(booking.getShow().getId());
        bookingDto.setUserId(booking.getUserId());
        bookingDto.setSeatsBooked(booking.getSeatsBooked());
        return bookingDto;
    }
}
