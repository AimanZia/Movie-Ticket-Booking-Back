package Wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Wallet.entities.Wallet;
import Wallet.entities.WalletUpdateRequest;
import Wallet.exceptions.ResourceNotFoundException;
import Wallet.service.WalletService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/wallets")
@CrossOrigin(origins = {"http://localhost:8080/users","http://localhost:8081/bookings"})
public class WalletController {
    
    @Autowired
    private WalletService walletService;

    @GetMapping("/{userId}")
    public ResponseEntity<Wallet> getWalletDetails(@PathVariable Integer userId) {
        Wallet walletDetailsByUser = this.walletService.getWalletDetailsByUser(userId);
        if(walletDetailsByUser == null){
            throw new ResourceNotFoundException("Wallet Not Found","user ID",userId.longValue());
        }
        return ResponseEntity.status(HttpStatus.OK).body(walletDetailsByUser);
    }
    
    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateWallet(@PathVariable Integer userId, @RequestBody WalletUpdateRequest request) {
        String userStatus = this.walletService.chcekIfUserExists(userId);
        
        if(userStatus==null || !(userStatus).equals("200 OK")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Does not Exists");
        }

        Wallet wallet = this.walletService.getWalletDetailsByUser(userId);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUserId(userId);
            wallet.setBalance(0.0);
        }

        if ("debit".equals(request.getAction())) {
            if (wallet.getBalance() < request.getAmount()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance");
            }
            wallet.setBalance(wallet.getBalance() - request.getAmount());
        } else if ("credit".equals(request.getAction())) {
            if (request.getAmount()<0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong Amount");
            }
            wallet.setBalance(wallet.getBalance() + request.getAmount());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid action");
        }

        Wallet savedWallet = this.walletService.saveWallet(wallet);
        return ResponseEntity.ok(savedWallet);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteWallet(@PathVariable Integer userId) {
        Wallet wallet = this.walletService.getWalletDetailsByUser(userId);
        if (wallet != null) {
            this.walletService.delete(wallet);
            return ResponseEntity.ok("Wallet deleted successfully for user: " + userId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wallet not found for user: " + userId);
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAllWallets() {
        this.walletService.deleteAll();
        return ResponseEntity.ok("All wallets deleted successfully");
    }
}
