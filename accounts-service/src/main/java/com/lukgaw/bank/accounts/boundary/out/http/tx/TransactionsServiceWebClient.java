package com.lukgaw.bank.accounts.boundary.out.http.tx;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class TransactionsServiceWebClient implements TransactionsService {
    private final WebClient webClient;

    @Override
    public Mono<AccountTransactionsDTO> getAccountTransactions(UUID accountId, Pageable pageRequest) {
        return webClient.get().uri("/transactions/{accountId}", Map.of("accountId", accountId))
                .exchangeToMono(res -> res.bodyToMono(AccountTransactionsDTO.class));
    }
}
