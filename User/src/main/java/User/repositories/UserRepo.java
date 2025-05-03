package User.repositories;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import User.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User,Integer>{

    Optional<User> findByEmail(String email);
} 
