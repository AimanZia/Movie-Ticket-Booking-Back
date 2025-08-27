package Wallet.entities.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WalletUpdateRequestDTO {
    @NotBlank(message = "Action must be credit or debit")
    @JsonProperty("action")
    private String action;

    @NotNull(message = "Amount could not be null")
    @Min(value = 0,message = "Amount must be non negative")
    @JsonProperty("amount")
    private Integer amount;
}
