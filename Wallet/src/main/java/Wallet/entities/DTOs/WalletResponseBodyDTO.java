package Wallet.entities.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponseBodyDTO {
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("balance")
    private Double balance;
}
