package User.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import User.entities.User;

public interface UserRepo extends JpaRepository<User,Integer>{

    public User findByEmail(String email);
} 
