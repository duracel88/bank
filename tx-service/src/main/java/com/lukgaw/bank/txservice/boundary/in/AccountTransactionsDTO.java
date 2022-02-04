package com.lukgaw.bank.txservice.boundary.in;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class AccountTransactionsDTO {
    private UUID accountId;
    private List<TransactionDTO> transactions;

    @Builder
    @Getter
    public static class TransactionDTO {
        private UUID id;
        private Boolean isDeposit;
        private Currency currency;
        private Long orderId;
        private BigDecimal transactionAmount;
        private BigDecimal amountAfter;
    }

}
