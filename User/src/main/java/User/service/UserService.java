package User.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import User.entities.User;
import User.exceptions.ResourceNotFoundException;
import User.extrenal.services.BookingService;
import User.repositories.UserRepo;
import jakarta.transaction.Transactional;

@Service
@Transactional
@Component
public class UserService {
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${wallet.url}")
    private String walletUrl;

    @Value("${bookings.url}")
    private String bookingsUrl;

    @Value("${bookings.user.url}")
    private String bookingsUserUrl;

    @Autowired
    private BookingService bookingServiceClient;

    public User findByEmail(String userEmail){
        return this.userRepo.findByEmail(userEmail);
    }

    public User createUser(User user){
        return this.userRepo.save(user);
    }

    public User findUserById(Integer userId){
        return this.userRepo.findById(userId).orElseThrow((()-> new ResourceNotFoundException("User not found")));
    }

    public void deleteUser(Integer userId){
        User searchedUser = this.userRepo.findById(userId).orElseThrow((()-> new ResourceNotFoundException("User not found")));
        String deleteBookingsByUserCode = deleteBookingsByUser(userId);
        if(!deleteBookingsByUserCode.equals("200 OK")){
            throw new ResourceNotFoundException("Booking not found");
        }
        String deleteWalletByUserCode = deleteWalletByUser(userId);
        if(!deleteWalletByUserCode.equals("200 OK")){
            throw new ResourceNotFoundException("Wallet not found for User");
        }
        this.userRepo.delete(searchedUser);
    }

    public void deleteAllUsers(){
        deleteAllBookingWallet();
        this.userRepo.deleteAll();
    }

    private void deleteAllBookingWallet() {
        String url = walletUrl;

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    entity,
                    String.class
            );

            System.out.println("Status code: " + responseEntity.getStatusCode());
            System.out.println("Response body: " + responseEntity.getBody());
        }
        catch(Exception e){
            e.printStackTrace();
        }

        String urlBooking = bookingsUrl;

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    urlBooking,
                    HttpMethod.DELETE,
                    entity,
                    String.class
            );

            System.out.println("Status code: " + responseEntity.getStatusCode());
            System.out.println("Response body: " + responseEntity.getBody());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private String deleteBookingsByUser(Integer userId){
        String url = bookingsUserUrl+userId;

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
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

    private String deleteWalletByUser(Integer userId){
        String url = walletUrl+"/"+userId;

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
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
}
