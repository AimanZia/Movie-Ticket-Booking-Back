package Wallet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import Wallet.entities.Wallet;
import Wallet.external.services.UserService;
import Wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserService userServiceClient;

    @Value("${wallet.user.url}")
    private String walletUserUrl;

    public WalletService(WalletRepository walletRepository,UserService userServiceClient){
        this.walletRepository = walletRepository;
        this.userServiceClient = userServiceClient;
    }

    public Wallet getWalletDetailsByUser(Integer userId) {
        return  this.walletRepository.findByUserId(userId);   
    }

    public Wallet saveWallet(Wallet wallet) {
        return this.walletRepository.save(wallet);
    }

    public void delete(Wallet wallet) {
       this.walletRepository.delete(wallet);
    }

    public void deleteAll() {
       this.walletRepository.deleteAll();
    }

    public Boolean chcekIfUserExists(Integer userId){
        try {
            return this.userServiceClient.checkIFUserExists(userId).getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
           return false;
        }
    }
}
