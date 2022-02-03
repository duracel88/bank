package com.lukgaw.bank.accounts.boundary.out.http.tx;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Component
public class DummyTransactionsService implements TransactionsService {

    @Override
    public Mono<AccountTransactionsDTO> getAccountTransactions(UUID accountId, Pageable pageRequest) {
        return Mono.just(AccountTransactionsDTO.builder()
                .accountId(accountId)
                .transactions(new PageImpl<>(List.of(
                        AccountTransactionsDTO.TransactionDTO.builder()
                                .id(UUID.randomUUID())
                                .amountAfter(BigDecimal.valueOf(20.01D))
                                .currency(Currency.getInstance("PLN"))
                                .isDeposit(true)
                                .orderId(1L)
                                .transactionAmount(BigDecimal.valueOf(20.10D))
                                .build()
                ), pageRequest, 0))
                .build());
    }
}
