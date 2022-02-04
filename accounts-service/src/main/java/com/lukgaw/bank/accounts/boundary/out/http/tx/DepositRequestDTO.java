package com.lukgaw.bank.accounts.boundary.out.http.tx;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
public class DepositRequestDTO {
    @NonNull UUID targetAccountId;
    @NonNull BigDecimal depositAmount;
    @NonNull String currency;
}
