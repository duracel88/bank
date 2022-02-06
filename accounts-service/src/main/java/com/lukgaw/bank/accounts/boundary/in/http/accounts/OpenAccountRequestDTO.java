package com.lukgaw.bank.accounts.boundary.in.http.accounts;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
public class OpenAccountRequestDTO {
    @NotNull @Size(min = 1, max = 32)
    String firstName;
    @NotNull @Size(min = 1, max = 32)
    String lastName;
    @Positive
    BigDecimal firstDepositAmount;

    @NotNull
    @Pattern(regexp = "[A-Z]{3}")
    String currency;
}
