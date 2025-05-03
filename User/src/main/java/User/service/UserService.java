package User.service;


import java.util.Optional;
import org.springframework.stereotype.Service;
import User.entities.User;
import User.exceptions.ResourceNotFoundException;
import User.external.services.BookingService;
import User.external.services.WalletService;
import User.repositories.UserRepo;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepo userRepo;
    private final BookingService bookingServiceClient;
    private final WalletService walletSerivceClient;

    public UserService(UserRepo userRepo,BookingService bookingService,WalletService walletService){
        this.userRepo = userRepo;
        this.bookingServiceClient  = bookingService;
        this.walletSerivceClient = walletService;
    }

    public Optional<User> findByEmail(String userEmail){
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

        if(!deleteBookingsByUser(userId)){
            throw new ResourceNotFoundException("Booking For User not found");
        }
        if(!deleteWalletByUser(userId)){
            throw new ResourceNotFoundException("Wallet for User Not found");
        }
        this.userRepo.delete(searchedUser);
    }

    public void deleteAllUsers(){
        deleteAllBookingWallet();
        this.userRepo.deleteAll();
    }

    private void deleteAllBookingWallet() {
        this.bookingServiceClient.deleteAllUserBookings();
        this.walletSerivceClient.deleteAllUserWallet();
    }

    private boolean deleteBookingsByUser(Integer userId){
        return this.bookingServiceClient.deleteBookingByUser(userId).getStatusCode().is2xxSuccessful();
    }

    private boolean deleteWalletByUser(Integer userId){
        return this.walletSerivceClient.deleteWalletByUser(userId).getStatusCode().is2xxSuccessful();
    }
}
