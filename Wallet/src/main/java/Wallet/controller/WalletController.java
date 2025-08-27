package Wallet.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Wallet.entities.Wallet;
import Wallet.entities.DTOs.WalletResponseBodyDTO;
import Wallet.entities.DTOs.WalletUpdateRequestDTO;
import Wallet.exceptions.ResourceNotFoundException;
import Wallet.service.WalletService;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/wallets")
@CrossOrigin(origins = {"http://localhost:8080/users","http://localhost:8081/bookings"})
@Validated
public class WalletController {
    
    private final WalletService walletService;

    private static final String MESSAGE = "message";

    public WalletController(WalletService walletService){
        this.walletService = walletService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponseBodyDTO> getWalletDetails(@PathVariable Integer userId) {
        Wallet walletDetailsByUser = this.walletService.getWalletDetailsByUser(userId);
        if(walletDetailsByUser == null){
            throw new ResourceNotFoundException("Wallet Not Found","user ID",userId.longValue());
        }
        WalletResponseBodyDTO walletResponse = new WalletResponseBodyDTO(walletDetailsByUser.getUserId(),walletDetailsByUser.getBalance());
        return ResponseEntity.status(HttpStatus.OK).body(walletResponse);
    }
    
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateWallet(@PathVariable Integer userId, @Valid @RequestBody WalletUpdateRequestDTO request) {
        System.out.println(request.getAction());
        System.out.println(request.getAmount());
        boolean userStatus = this.walletService.chcekIfUserExists(userId);
        
        if(!userStatus){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE,"User Does not Exists"));
        }

        Wallet wallet = this.walletService.getWalletDetailsByUser(userId);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUserId(userId);
            wallet.setBalance(0.0);
        }

        if ("debit".equals(request.getAction())) {
            if (wallet.getBalance() < request.getAmount()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE,"Insufficient balance"));
            }
            wallet.setBalance(wallet.getBalance() - request.getAmount());
        } else if ("credit".equals(request.getAction())) {
            if (request.getAmount()<0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE,"Wrong Amount"));
            }
            wallet.setBalance(wallet.getBalance() + request.getAmount());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(MESSAGE,"Invalid action"));
        }

        Wallet savedWallet = this.walletService.saveWallet(wallet);
        WalletResponseBodyDTO walletResponse = new WalletResponseBodyDTO(savedWallet.getUserId(), savedWallet.getBalance());
        return ResponseEntity.ok(walletResponse);
    }

    @DeleteMapping(value="/{userId}",produces=MediaType.APPLICATION_JSON)
    public ResponseEntity<Object> deleteWallet(@PathVariable Integer userId) {
        Wallet wallet = this.walletService.getWalletDetailsByUser(userId);
        if (wallet != null) {
            this.walletService.delete(wallet);
            return ResponseEntity.ok(Map.of(MESSAGE,"Wallet deleted successfully for user: " + userId));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MESSAGE,"Wallet not found for user: " + userId));
        }
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Object> deleteAllWallets() {
        this.walletService.deleteAll();
        return ResponseEntity.ok(Map.of(MESSAGE,"All wallets deleted successfully"));
    }
}
