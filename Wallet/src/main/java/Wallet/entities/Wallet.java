package Wallet.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
public class Wallet {

    @Id
    @Column(nullable = false)
    @JsonProperty("user_id")
    private Integer userId;

    @Column(nullable = false)
    @JsonProperty("user_id")
    private Double balance;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}

