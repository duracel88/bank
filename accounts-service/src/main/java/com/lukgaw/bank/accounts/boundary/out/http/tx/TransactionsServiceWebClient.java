package com.lukgaw.bank.accounts.boundary.out.http.tx;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class TransactionsServiceWebClient implements TransactionsService {
    private final WebClient webClient;
    private final Settings settings;

    @AllArgsConstructor(staticName = "of")
    public static final class Settings {
        private int maxAttempts = 3;
        private int backoffSeconds = 1;
    }

    @Override
    public Mono<AccountTransactionsDTO> getAccountTransactions(UUID accountId, Pageable pageRequest) {
        return webClient.get().uri("/transactions/{accountId}", Map.of("accountId", accountId))
                .exchangeToMono(res -> res.bodyToMono(AccountTransactionsDTO.class))
                .retryWhen(Retry.backoff(settings.maxAttempts, Duration.ofSeconds(settings.backoffSeconds)));
    }

    @Override
    public Mono<DepositResponseDTO> makeDeposit(DepositRequestDTO request) {
        return webClient.post().uri("/deposit")
                .bodyValue(request)
                .exchangeToMono(res -> res.bodyToMono(DepositResponseDTO.class))
                .retryWhen(Retry.backoff(settings.maxAttempts, Duration.ofSeconds(settings.backoffSeconds)));
    }
}
