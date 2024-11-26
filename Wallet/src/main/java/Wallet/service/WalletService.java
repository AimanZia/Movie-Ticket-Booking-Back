package Wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import Wallet.entities.Wallet;
import Wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class WalletService {
    
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${wallet.user.url}")
    private String walletUserUrl;

    public Wallet getWalletDetailsByUser(Integer userId) {
        Wallet wallet = this.walletRepository.findByUserId(userId);   
        return wallet;
    }

    public Wallet saveWallet(Wallet wallet) {
        Wallet savedWallet = this.walletRepository.save(wallet);
        return savedWallet;
    }

    public void delete(Wallet wallet) {
       this.walletRepository.delete(wallet);
    }

    public void deleteAll() {
       this.walletRepository.deleteAll();
    }

    public String chcekIfUserExists(Integer userId){
        String url = walletUserUrl+userId;

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            System.out.println("Status code: " + responseEntity.getStatusCode());
            System.out.println("Response body: " + responseEntity.getBody());

            return responseEntity.getStatusCode().toString();
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
