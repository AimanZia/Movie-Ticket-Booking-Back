package User.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import User.entities.User;
import User.entities.DTOs.UserRequestDTO;
import User.exceptions.ResourceNotFoundException;
import User.external.services.BookingService;
import User.external.services.WalletService;
import User.repositories.UserRepo;
import User.service.UserService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    
    private final UserRepo userRepo;
    private final BookingService bookingServiceClient;
    private final WalletService walletSerivceClient;

    public UserServiceImpl(UserRepo userRepo,BookingService bookingService,WalletService walletService){
        this.userRepo = userRepo;
        this.bookingServiceClient  = bookingService;
        this.walletSerivceClient = walletService;
    }

    @Override
    public Optional<User> findByEmail(String userEmail){
        return this.userRepo.findByEmail(userEmail);
    }

    @Override
    public User createUser(UserRequestDTO user){
        User userTObeSaved = new User();
        userTObeSaved.setName(user.getName()); // later replace with MapStruct - MapStruct is a compile-time code generator that creates mappers to automatically convert between Java objects (e.g., DTOs and Entities). It saves you from writing and maintaining boilerplate mapping code.
        userTObeSaved.setEmail(user.getEmail());
        return this.userRepo.save(userTObeSaved);
    }

    @Override
    public User findUserById(Integer userId){
        return this.userRepo.findById(userId).orElseThrow((()-> new ResourceNotFoundException("User not found")));
    }

    @Override
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

    @Override
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
