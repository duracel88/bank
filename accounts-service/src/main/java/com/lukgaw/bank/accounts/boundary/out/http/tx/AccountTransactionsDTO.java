package com.lukgaw.bank.accounts.boundary.out.http.tx;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Builder
@Getter
public class AccountTransactionsDTO {
    private UUID accountId;
    private Page<TransactionDTO> transactions;

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
