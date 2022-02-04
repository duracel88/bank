package com.lukgaw.bank.accounts.boundary.out.http.tx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AccountTransactionsDTO {
    private UUID accountId;
    private List<TransactionDTO> transactions;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class TransactionDTO {
        private UUID id;
        private Long orderId;
        private Boolean isDeposit;
        private Currency currency;
        private BigDecimal transactionAmount;
        private BigDecimal amountAfter;
    }

}
