package User.service;


import java.util.Optional;
import org.springframework.stereotype.Service;
import User.entities.User;
import User.entities.DTOs.UserRequestDTO;
import User.exceptions.ResourceNotFoundException;
import User.external.services.BookingService;
import User.external.services.WalletService;
import User.repositories.UserRepo;
import jakarta.transaction.Transactional;


public interface UserService {

    public Optional<User> findByEmail(String userEmail);
    public User createUser(UserRequestDTO user);
    public User findUserById(Integer userId);
    public void deleteUser(Integer userId);
    public void deleteAllUsers();
}
