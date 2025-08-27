package Wallet.service;

import Wallet.entities.Wallet;

public interface WalletService {

   public Wallet getWalletDetailsByUser(Integer userId);
   public Wallet saveWallet(Wallet wallet);
   public void delete(Wallet wallet);
   public void deleteAll();

   public Boolean chcekIfUserExists(Integer userId); // Check if user exists by calling user service
}
