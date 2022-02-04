package com.lukgaw.bank.txservice.boundary.in;

import com.lukgaw.bank.txservice.domain.model.AccountId;
import com.lukgaw.bank.txservice.domain.model.AccountTransactionRecord;
import com.lukgaw.bank.txservice.domain.model.Money;
import com.lukgaw.bank.txservice.domain.model.TransactionId;
import com.lukgaw.bank.txservice.domain.port.FindTransactionsPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest(TransactionResource.class)
class TransactionResourceContractTest extends ContractTest {

    @MockBean
    FindTransactionsPort findTransactionsPort;

    @Autowired
    WebTestClient web;

    @Test
    public void should_return_200_on_empty_list() {
        AccountId accountId = AccountId.of(UUID.randomUUID());
        Mockito.when(findTransactionsPort.findAccountTransactionRecords(eq(accountId), any())).thenReturn(Flux.empty());

        web.mutateWith(SecurityMockServerConfigurers.mockJwt())
                .get().uri("/transactions/{accountId}", Map.of("accountId", accountId.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.accountId").value(equalTo(accountId.getId().toString()))
                .jsonPath("$.transactions").isArray()
                .jsonPath("$.transactions").isEmpty();

    }

    @Test
    public void should_return_200_on_non_empty_list() {
        AccountId accountId = AccountId.of(UUID.randomUUID());
        AccountTransactionRecord accountTransactionRecord = AccountTransactionRecord.builder()
                .transactionId(TransactionId.of(UUID.randomUUID()))
                .accountId(AccountId.of(UUID.randomUUID()))
                .amountAfter(Money.of(20.22D, "PLN"))
                .transferAmount(Money.of(20.22D, "PLN"))
                .orderId(1L)
                .build();
        Mockito.when(findTransactionsPort.findAccountTransactionRecords(eq(accountId), any())).thenReturn(Flux.just(accountTransactionRecord));

        web.mutateWith(SecurityMockServerConfigurers.mockJwt())
                .get().uri("/transactions/{accountId}", Map.of("accountId", accountId.getId()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.accountId").value(equalTo(accountId.getId().toString()))
                .jsonPath("$.transactions").isArray()
                .jsonPath("$.transactions[0].id").value(equalTo(accountTransactionRecord.getTransactionId().getId().toString()))
                .jsonPath("$.transactions[0].isDeposit").value(equalTo(true))
                .jsonPath("$.transactions[0].currency").value(equalTo("PLN"))
                .jsonPath("$.transactions[0].orderId").value(equalTo(1))
                .jsonPath("$.transactions[0].transactionAmount").value(equalTo(20.22D))
                .jsonPath("$.transactions[0].amountAfter").value(equalTo(20.22D));
    }

    @Test
    public void should_return_403_if_is_not_authenticated() {
        web.get().uri("/transactions/{accountId}", Map.of("accountId", "uuid"))
                .exchange()
                .expectStatus()
                .isUnauthorized();

    }

    @Test
    public void should_return_500_if_transaction_failed() {
        AccountId accountId = AccountId.of(UUID.randomUUID());
        Mockito.when(findTransactionsPort.findAccountTransactionRecords(eq(accountId), any()))
                .thenReturn(Flux.error(Exception::new));

        web.mutateWith(SecurityMockServerConfigurers.mockJwt())
                .get().uri("/transactions/{accountId}", Map.of("accountId", accountId.getId()))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}