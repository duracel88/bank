package com.lukgaw.bank.accounts.boundary.out.http.tx;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TransactionsService {
    Mono<AccountTransactionsDTO> getAccountTransactions(UUID accountId, Pageable pageRequest);

    default Mono<AccountTransactionsDTO> getLastAccountTransactions(UUID accountId, int transactionsCount) {
        return getAccountTransactions(accountId, PageRequest.of(0, transactionsCount));
    }
}
