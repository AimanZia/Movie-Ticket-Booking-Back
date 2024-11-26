package Wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Wallet.entities.Wallet;

public interface WalletRepository extends JpaRepository<Wallet,Integer>{
    Wallet findByUserId(Integer userId);
}
