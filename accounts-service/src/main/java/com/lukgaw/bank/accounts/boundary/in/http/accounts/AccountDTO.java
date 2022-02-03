package com.lukgaw.bank.accounts.boundary.in.http.accounts;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class AccountDTO {
    private UUID accountId;
    private UUID customerId;
    private String firstName;
    private String lastName;
    private BigDecimal balance;
    private String currency;
    private List<TransactionDTO> transactions;

    @Builder
    @Getter
    public static class TransactionDTO {
        private UUID id;
        private BigDecimal amount;
        private Long orderId;
    }

}
