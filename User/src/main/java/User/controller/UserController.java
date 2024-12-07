package User.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import User.entities.User;
import User.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"http://localhost:8081/bookings","http://localhost:8082/wallets"})
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User request) {
        if (userService.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        User user = this.userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Integer userId) {
        User userById = this.userService.findUserById(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userById);
    }
    
    @DeleteMapping("/{userId}")            
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {    
        this.userService.deleteUser(userId);    
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsers() {
        this.userService.deleteAllUsers();
        return ResponseEntity.ok().build();
    }
}
