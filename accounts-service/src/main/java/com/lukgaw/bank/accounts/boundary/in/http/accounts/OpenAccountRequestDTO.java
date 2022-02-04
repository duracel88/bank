package com.lukgaw.bank.accounts.boundary.in.http.accounts;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class OpenAccountRequestDTO {
    @NotNull private UUID customerId;
    @NotNull private String firstName;
    @NotNull private String lastName;
    private BigDecimal firstDepositAmount;

    @NotNull
    @Pattern(regexp = "[A-Z]{3}")
    private String currency;
}
