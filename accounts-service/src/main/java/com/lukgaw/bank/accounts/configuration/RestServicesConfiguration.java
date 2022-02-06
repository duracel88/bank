package com.lukgaw.bank.accounts.configuration;

import com.lukgaw.bank.accounts.boundary.out.http.LoggingWebClient;
import com.lukgaw.bank.accounts.boundary.out.http.tx.TransactionsService;
import com.lukgaw.bank.accounts.boundary.out.http.tx.TransactionsServiceWebClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RestServicesConfiguration {

    @Bean
    @Qualifier("Tx-service")
    public WebClient transactionsWebClient(@Value("${services.tx-service.host}") String baseUrl) {
        return new LoggingWebClient(baseUrl);
    }

    @Bean
    public TransactionsService transactionsService(@Qualifier("Tx-service") WebClient webClient,
                                                   @Value("${services.tx-service.retry.backoff.maxAttempts}") int maxAttempts,
                                                   @Value("${services.tx-service.retry.backoff.backoffSeconds}") int backoffSeconds) {
        return new TransactionsServiceWebClient(webClient, TransactionsServiceWebClient.Settings.of(maxAttempts, backoffSeconds));
    }
}
