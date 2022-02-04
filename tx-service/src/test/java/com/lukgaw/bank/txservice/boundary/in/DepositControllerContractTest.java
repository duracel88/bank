package com.lukgaw.bank.txservice.boundary.in;

import com.lukgaw.bank.txservice.domain.model.AccountId;
import com.lukgaw.bank.txservice.domain.model.Money;
import com.lukgaw.bank.txservice.domain.model.TransactionId;
import com.lukgaw.bank.txservice.domain.port.CreateDepositPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static com.lukgaw.bank.txservice.domain.port.CreateDepositPort.DepositResult.duplicatedOrderId;
import static com.lukgaw.bank.txservice.domain.port.CreateDepositPort.DepositResult.successfulDeposit;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest(DepositController.class)
class DepositControllerContractTest extends ContractTest {

    @MockBean
    CreateDepositPort createDepositPort;

    @Autowired
    WebTestClient web;

    @Test
    public void should_return_400_when_targetAccountId_is_null() {
        var body = new DepositController.DepositRequest();
        body.currency = "PLN";
        body.targetAccountId = null;
        body.depositAmount = BigDecimal.valueOf(20.20);

        web.mutateWith(SecurityMockServerConfigurers.mockJwt())
                .mutateWith(SecurityMockServerConfigurers.mockJwt())
                .post().uri("/deposit")
                .bodyValue(body)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    public void should_return_400_when_depositAmount_is_null() {
        var body = new DepositController.DepositRequest();
        body.currency = "PLN";
        body.targetAccountId = UUID.randomUUID();
        body.depositAmount = null;

        web.mutateWith(SecurityMockServerConfigurers.mockJwt())
                .mutateWith(SecurityMockServerConfigurers.mockJwt())
                .post().uri("/deposit")
                .bodyValue(body)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    public void should_return_400_when_currency_is_null() {
        var body = new DepositController.DepositRequest();
        body.currency = null;
        body.targetAccountId = UUID.randomUUID();
        body.depositAmount = BigDecimal.valueOf(20.20);

        web.mutateWith(SecurityMockServerConfigurers.mockJwt())
                .mutateWith(SecurityMockServerConfigurers.mockJwt())
                .post().uri("/deposit")
                .bodyValue(body)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    public void should_return_400_when_depositAmount_is_zero() {
        var body = new DepositController.DepositRequest();
        body.currency = "PLN";
        body.targetAccountId = UUID.randomUUID();
        body.depositAmount = BigDecimal.ZERO;

        web.mutateWith(SecurityMockServerConfigurers.mockJwt())
                .mutateWith(SecurityMockServerConfigurers.mockJwt())
                .post().uri("/deposit")
                .bodyValue(body)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    public void should_return_400_when_depositAmount_is_negative() {
        var body = new DepositController.DepositRequest();
        body.currency = "PLN";
        body.targetAccountId = UUID.randomUUID();
        body.depositAmount = BigDecimal.valueOf(-1.00D);

        web.mutateWith(SecurityMockServerConfigurers.mockJwt())
                .mutateWith(SecurityMockServerConfigurers.mockJwt())
                .post().uri("/deposit")
                .bodyValue(body)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    public void should_return_400_when_body_is_missing() {
        web.mutateWith(SecurityMockServerConfigurers.mockJwt())
                .mutateWith(SecurityMockServerConfigurers.mockJwt())
                .post().uri("/deposit")
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    public void should_return_200_when_request_is_valid() {
        var body = new DepositController.DepositRequest();
        body.currency = "PLN";
        body.targetAccountId = UUID.randomUUID();
        body.depositAmount = BigDecimal.valueOf(1.00D);

        Mockito.when(createDepositPort.deposit(eq(AccountId.of(body.targetAccountId)), eq(Money.of(body.depositAmount, Currency.getInstance("PLN")))))
                .thenReturn(Mono.just(successfulDeposit(TransactionId.of(UUID.randomUUID()))));

        web.mutateWith(SecurityMockServerConfigurers.mockJwt())
                .mutateWith(SecurityMockServerConfigurers.mockJwt())
                .post().uri("/deposit")
                .bodyValue(body)
                .exchange()
                .expectStatus()
                .isOk();
    }


    @Test
    public void should_return_412_when_request_is_valid() {
        var body = new DepositController.DepositRequest();
        body.currency = "PLN";
        body.targetAccountId = UUID.randomUUID();
        body.depositAmount = BigDecimal.valueOf(1.00D);

        Mockito.when(createDepositPort.deposit(eq(AccountId.of(body.targetAccountId)), eq(Money.of(body.depositAmount, Currency.getInstance("PLN")))))
                .thenReturn(Mono.just(duplicatedOrderId()));

        web.mutateWith(SecurityMockServerConfigurers.mockJwt())
                .mutateWith(SecurityMockServerConfigurers.mockJwt())
                .post().uri("/deposit")
                .bodyValue(body)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    public void should_return_500_when_unexpected_error_occurred() {
        var body = new DepositController.DepositRequest();
        body.currency = "PLN";
        body.targetAccountId = UUID.randomUUID();
        body.depositAmount = BigDecimal.valueOf(1.00D);

        Mockito.when(createDepositPort.deposit(eq(AccountId.of(body.targetAccountId)), eq(Money.of(body.depositAmount, Currency.getInstance("PLN")))))
                .thenReturn(Mono.just(CreateDepositPort.DepositResult.failedDeposit("Failed")));

        web.mutateWith(SecurityMockServerConfigurers.mockJwt())
                .mutateWith(SecurityMockServerConfigurers.mockJwt())
                .post().uri("/deposit")
                .bodyValue(body)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}