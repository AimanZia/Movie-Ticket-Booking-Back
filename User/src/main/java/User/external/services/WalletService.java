package User.external.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "WALLET-SERVICE")
public interface WalletService {
    
    @DeleteMapping("/wallets/{user_id}")
    ResponseEntity<?> deleteWalletByUser(@PathVariable("user_id") Integer userId);

    @DeleteMapping("/wallets")
    ResponseEntity<?> deleteAllUserWallet();
}
