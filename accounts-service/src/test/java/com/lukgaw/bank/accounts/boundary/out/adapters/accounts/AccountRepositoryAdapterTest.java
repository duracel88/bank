package com.lukgaw.bank.accounts.boundary.out.adapters.accounts;

import com.lukgaw.bank.accounts.boundary.out.http.tx.AccountTransactionsDTO;
import com.lukgaw.bank.accounts.boundary.out.http.tx.AccountTransactionsDTO.TransactionDTO;
import com.lukgaw.bank.accounts.boundary.out.http.tx.TransactionsService;
import com.lukgaw.bank.accounts.boundary.out.r2dbc.R2dbcConfiguration;
import com.lukgaw.bank.accounts.boundary.out.r2dbc.account.AccountDetailsReactiveCrudRepository;
import com.lukgaw.bank.accounts.domain.common.Money;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static com.lukgaw.bank.accounts.boundary.out.r2dbc.DatabaseAccounts.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;

@DataR2dbcTest
@Import({R2dbcConfiguration.class, AccountRepositoryAdapterTest.Configuration.class})
class AccountRepositoryAdapterTest {

    @TestConfiguration
    @ComponentScan(basePackages = "com.lukgaw.bank.accounts.boundary.out.r2dbc.converters")
    public static class Configuration {

        @Bean
        public AccountRepositoryAdapter accountRepositoryAdapter(AccountDetailsReactiveCrudRepository reactiveCrudRepository,
                                                                 TransactionsService transactionsService) {
            return new AccountRepositoryAdapter(reactiveCrudRepository, transactionsService);
        }
    }

    @MockBean
    TransactionsService transactionsService;

    @Autowired
    AccountRepositoryAdapter accountRepositoryAdapter;

    @Test
    public void should_return_basic_details_with_empty_transaction_list() {
        Mockito.when(transactionsService.getLastAccountTransactions(eq(CHUCK_ACCOUNT_ID.getId()), anyInt()))
                .thenReturn(Mono.empty());
        accountRepositoryAdapter.findAccountById(CHUCK_ACCOUNT_ID, 50)
                .as(StepVerifier::create)
                .consumeNextWith(account -> {
                    assertEquals(CHUCK_ACCOUNT_ID, account.getAccountId());
                    assertEquals(CHUCK_CUSTOMER_ID, account.getOwnerDetails().getCustomerId());
                    assertEquals(CHUCK_NAME, account.getOwnerDetails().getFirstName());
                    assertEquals(Money.zero(Currency.getInstance("PLN")), account.getBalance());
                    assertEquals(CHUCK_LASTNAME, account.getOwnerDetails().getLastName());
                    assertTrue(account.getTransactions().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    public void should_return_basic_details_with_transaction_list() {
        Mockito.when(transactionsService.getLastAccountTransactions(eq(CHUCK_ACCOUNT_ID.getId()), anyInt()))
                .thenReturn(Mono.just(AccountTransactionsDTO.builder()
                        .accountId(CHUCK_ACCOUNT_ID.getId())
                        .transactions(List.of(TransactionDTO.builder()
                                .id(UUID.randomUUID())
                                .orderId(1L)
                                .isDeposit(true)
                                .currency(Currency.getInstance("PLN"))
                                .transactionAmount(BigDecimal.TEN)
                                .amountAfter(BigDecimal.TEN)
                                .build()))
                        .build()));
        accountRepositoryAdapter.findAccountById(CHUCK_ACCOUNT_ID, 50)
                .as(StepVerifier::create)
                .consumeNextWith(account -> {
                    assertEquals(CHUCK_ACCOUNT_ID, account.getAccountId());
                    assertEquals(CHUCK_CUSTOMER_ID, account.getOwnerDetails().getCustomerId());
                    assertEquals(CHUCK_NAME, account.getOwnerDetails().getFirstName());
                    assertEquals(CHUCK_LASTNAME, account.getOwnerDetails().getLastName());
                    assertEquals(Money.of(BigDecimal.TEN, Currency.getInstance("PLN")), account.getBalance());
                    assertEquals(1, account.getTransactions().size());
                })
                .verifyComplete();
    }

    @Test
    public void should_return_basic_details_with_all_transactions_and_balance() {
        Mockito.when(transactionsService.getLastAccountTransactions(eq(CHUCK_ACCOUNT_ID.getId()), anyInt()))
                .thenReturn(Mono.just(AccountTransactionsDTO.builder()
                        .accountId(CHUCK_ACCOUNT_ID.getId())
                        .transactions(List.of(
                                TransactionDTO.builder()
                                        .id(UUID.randomUUID())
                                        .orderId(1L)
                                        .isDeposit(true)
                                        .currency(Currency.getInstance("PLN"))
                                        .transactionAmount(BigDecimal.TEN)
                                        .amountAfter(BigDecimal.TEN)
                                        .build(),
                                TransactionDTO.builder()
                                        .id(UUID.randomUUID())
                                        .orderId(2L)
                                        .isDeposit(true)
                                        .currency(Currency.getInstance("PLN"))
                                        .transactionAmount(BigDecimal.TEN)
                                        .amountAfter(BigDecimal.valueOf(20.D))
                                        .build()))
                        .build()));
        accountRepositoryAdapter.findAccountById(CHUCK_ACCOUNT_ID, 50)
                .as(StepVerifier::create)
                .consumeNextWith(account -> {
                    assertEquals(CHUCK_ACCOUNT_ID, account.getAccountId());
                    assertEquals(CHUCK_CUSTOMER_ID, account.getOwnerDetails().getCustomerId());
                    assertEquals(CHUCK_NAME, account.getOwnerDetails().getFirstName());
                    assertEquals(CHUCK_LASTNAME, account.getOwnerDetails().getLastName());
                    assertEquals(Money.of(BigDecimal.valueOf(20.0D), Currency.getInstance("PLN")), account.getBalance());
                    assertEquals(2, account.getTransactions().size());
                })
                .verifyComplete();
    }


}