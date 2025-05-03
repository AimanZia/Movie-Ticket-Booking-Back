package Wallet.external.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("USER-SERVICE")
public interface UserService {

    @GetMapping("/users/{userId}")    
    ResponseEntity<?> checkIFUserExists(@PathVariable("userId") Integer userId);
}
