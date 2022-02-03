package com.lukgaw.bank.accounts.boundary.in.http.accounts;

import com.lukgaw.bank.accounts.bounday.in.http.ContractTest;
import com.lukgaw.bank.accounts.bounday.in.http.People;
import com.lukgaw.bank.accounts.domain.accounts.model.Account;
import com.lukgaw.bank.accounts.domain.accounts.model.AccountId;
import com.lukgaw.bank.accounts.domain.accounts.model.AccountOwner;
import com.lukgaw.bank.accounts.domain.accounts.model.TransactionPreview;
import com.lukgaw.bank.accounts.domain.accounts.port.AccountRepository;
import com.lukgaw.bank.accounts.domain.common.Money;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;

@WebFluxTest(AccountsResource.class)
class AccountsResourceContractTest extends ContractTest {

    @MockBean
    AccountRepository accountRepository;

    @Autowired
    WebTestClient web;

    private static Account JOE_ACCOUNT = Account.accountBuilder()
            .accountId(AccountId.of(UUID.randomUUID()))
            .balance(Money.of(20.00D, "PLN"))
            .transactions(List.of(TransactionPreview.builder()
                    .balanceAfter(Money.of(20.00D, "PLN"))
                    .orderId(1L)
                    .transactionId(UUID.randomUUID())
                    .transactionAmount(Money.of(20.00D, "PLN"))
                    .build()))
            .ownerDetails(AccountOwner.of(People.JoeCustomer.ID, "Luk", "Gaw"))
            .build();

    @Test
    public void should_return_401_when_user_is_not_authorized() {
        web
                .get().uri("/accounts/" + JOE_ACCOUNT.getAccountId().getId())
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    public void should_return_404_when_resource_is_not_found() {
        Mockito.when(accountRepository.findAccountById(any(), anyInt()))
                .thenReturn(Mono.empty());
        web.mutateWith(People.JoeCustomer.JWT_MUTATOR)
                .get().uri("/accounts/" + JOE_ACCOUNT.getAccountId().getId())
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void should_return_403_when_found_resource_doesnt_belong_to_customer() {
        Mockito.when(accountRepository.findAccountById(eq(JOE_ACCOUNT.getAccountId()), anyInt()))
                .thenReturn(Mono.just(JOE_ACCOUNT));
        web.mutateWith(People.SuseCustomer.JWT_MUTATOR)
                .get().uri("/accounts/" + JOE_ACCOUNT.getAccountId().getId())
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    public void should_return_200_and_body_when_resource_belongs_to_customer() {
        Mockito.when(accountRepository.findAccountById(eq(JOE_ACCOUNT.getAccountId()), anyInt()))
                .thenReturn(Mono.just(JOE_ACCOUNT));

        TransactionPreview transactionPreview = JOE_ACCOUNT.getTransactions().stream().findFirst().orElseThrow();
        web.mutateWith(People.JoeCustomer.JWT_MUTATOR)
                .get().uri("/accounts/" + JOE_ACCOUNT.getAccountId().getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.accountId").value(equalTo(JOE_ACCOUNT.getAccountId().getId().toString()))
                .jsonPath("$.customerId").value(equalTo(JOE_ACCOUNT.getOwnerDetails().getCustomerId().getId().toString()))
                .jsonPath("$.firstName").value(equalTo(JOE_ACCOUNT.getOwnerDetails().getFirstName()))
                .jsonPath("$.lastName").value(equalTo(JOE_ACCOUNT.getOwnerDetails().getLastName()))
                .jsonPath("$.balance").value(equalTo(JOE_ACCOUNT.getBalance().getValue().doubleValue()))
                .jsonPath("$.currency").value(equalTo(JOE_ACCOUNT.getBalance().getCurrency().getCurrencyCode()))
                .jsonPath("$.transactions[0].id").value(equalTo(transactionPreview.getTransactionId().toString()))
                .jsonPath("$.transactions[0].amount").value(equalTo(transactionPreview.getTransactionAmount().getValue().doubleValue()))
                .jsonPath("$.transactions[0].orderId").value(equalTo(transactionPreview.getOrderId().intValue()));


    }

    @Test
    public void when_admin_accesses_should_return_200_and_body_even_if_resource_doesnt_belong_to_the_user() {
        Mockito.when(accountRepository.findAccountById(eq(JOE_ACCOUNT.getAccountId()), anyInt()))
                .thenReturn(Mono.just(JOE_ACCOUNT));

        TransactionPreview transactionPreview = JOE_ACCOUNT.getTransactions().stream().findFirst().orElseThrow();
        web.mutateWith(People.DannyAdmin.JWT_MUTATOR)
                .get().uri("/accounts/" + JOE_ACCOUNT.getAccountId().getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.accountId").value(equalTo(JOE_ACCOUNT.getAccountId().getId().toString()))
                .jsonPath("$.customerId").value(equalTo(JOE_ACCOUNT.getOwnerDetails().getCustomerId().getId().toString()))
                .jsonPath("$.firstName").value(equalTo(JOE_ACCOUNT.getOwnerDetails().getFirstName()))
                .jsonPath("$.lastName").value(equalTo(JOE_ACCOUNT.getOwnerDetails().getLastName()))
                .jsonPath("$.balance").value(equalTo(JOE_ACCOUNT.getBalance().getValue().doubleValue()))
                .jsonPath("$.currency").value(equalTo(JOE_ACCOUNT.getBalance().getCurrency().getCurrencyCode()))
                .jsonPath("$.transactions[0].id").value(equalTo(transactionPreview.getTransactionId().toString()))
                .jsonPath("$.transactions[0].amount").value(equalTo(transactionPreview.getTransactionAmount().getValue().doubleValue()))
                .jsonPath("$.transactions[0].orderId").value(equalTo(transactionPreview.getOrderId().intValue()));
    }

    @Test
    public void customer_cant_access_endpoint_with_transactions_count_param(){
        web.mutateWith(People.SuseCustomer.JWT_MUTATOR)
                .get().uri("/accounts/" + JOE_ACCOUNT.getAccountId().getId() + "?size=10")
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    public void admin_can_access_endpoint_with_transactions_count_param(){
        Mockito.when(accountRepository.findAccountById(any(), anyInt()))
                .thenReturn(Mono.empty());
        web.mutateWith(People.DannyAdmin.JWT_MUTATOR)
                .get().uri("/accounts/" + JOE_ACCOUNT.getAccountId().getId() + "?size=10")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

}