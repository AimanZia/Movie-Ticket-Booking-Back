package User.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import User.entities.User;
import User.entities.DTOs.UserRequestDTO;
import User.entities.DTOs.UserResponseDTO;
import User.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"http://localhost:8081/bookings","http://localhost:8082/wallets"})
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequestDTO request) {

        if(userService.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        User user = this.userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") Integer userId) {
        User userById = this.userService.findUserById(userId);
        UserResponseDTO userResponseDTO = new UserResponseDTO(userById.getId(), userById.getName(), userById.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }
    
    @DeleteMapping("/{userId}")            
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId){    
        this.userService.deleteUser(userId);    
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsers(){
        this.userService.deleteAllUsers();
        return ResponseEntity.ok().build();
    }

}

