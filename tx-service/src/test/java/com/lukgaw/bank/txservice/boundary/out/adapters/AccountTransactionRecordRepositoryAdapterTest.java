package com.lukgaw.bank.txservice.boundary.out.adapters;

import com.lukgaw.bank.txservice.boundary.out.r2dbc.transactions.TransactionReactiveCrudRepository;
import com.lukgaw.bank.txservice.configuration.R2dbcConfiguration;
import com.lukgaw.bank.txservice.domain.model.AccountId;
import com.lukgaw.bank.txservice.domain.model.AccountTransactionRecord;
import com.lukgaw.bank.txservice.domain.model.Money;
import com.lukgaw.bank.txservice.domain.port.AccountTransactionRecordRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataR2dbcTest
@DirtiesContext()
@Import({R2dbcConfiguration.class, AccountTransactionRecordRepositoryAdapterTest.Configuration.class})
class AccountTransactionRecordRepositoryAdapterTest {

    @TestConfiguration
    @ComponentScan(basePackages = "com.lukgaw.bank.txservice.boundary.out.r2dbc.converters")
    public static class Configuration {

        @Bean
        public AccountTransactionRecordRepository accountTransactionRecordRepository(TransactionReactiveCrudRepository reactiveCrudRepository,
                                                                                     Supplier<UUID> transactionsService,
                                                                                     Supplier<LocalDateTime> localDateTimeSupplier) {
            return new AccountTransactionRecordRepositoryAdapter(reactiveCrudRepository, transactionsService, localDateTimeSupplier);
        }

        @Bean
        public Supplier<UUID> uuidGenMock() {
            return Mockito.mock(Supplier.class);
        }

        @Bean
        public Supplier<LocalDateTime> timeStampMock() {
            return Mockito.mock(Supplier.class);
        }
    }

    @Autowired
    Supplier<UUID> uuidGenMock;

    @Autowired
    Supplier<LocalDateTime> timeStampMock;

    @Autowired
    AccountTransactionRecordRepository recordRepository;

    @Test
    public void test() {
        UUID generatedUUID = UUID.randomUUID();
        LocalDateTime localDateTime = LocalDateTime.now();
        Mockito.when(uuidGenMock.get()).thenReturn(generatedUUID);
        Mockito.when(timeStampMock.get()).thenReturn(localDateTime);

        AccountTransactionRecord transaction = AccountTransactionRecord.builder()
                .transactionCorrelationId(null)
                .accountId(AccountId.of(UUID.randomUUID()))
                .amountAfter(Money.of(20.20, "PLN"))
                .transferAmount(Money.of(20.20, "PLN"))
                .orderId(1L)
                .build();

        recordRepository.saveTransaction(transaction)
                .as(StepVerifier::create)
                .consumeNextWith(transactionId -> assertEquals(generatedUUID, transactionId.getId()))
                .verifyComplete();
    }


}