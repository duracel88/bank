package com.lukgaw.bank.txservice.configuration;

import com.lukgaw.bank.txservice.boundary.out.adapters.AccountTransactionRecordRepositoryAdapter;
import com.lukgaw.bank.txservice.boundary.out.r2dbc.transactions.TransactionReactiveCrudRepository;
import com.lukgaw.bank.txservice.domain.port.AccountTransactionRecordRepository;
import com.lukgaw.bank.txservice.domain.port.CreateDepositPort;
import com.lukgaw.bank.txservice.domain.service.DepositService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class DomainTransactionConfiguration {

    @Bean
    public AccountTransactionRecordRepository accountTransactionRecordRepository(TransactionReactiveCrudRepository transactionReactiveCrudRepository) {
        return new AccountTransactionRecordRepositoryAdapter(transactionReactiveCrudRepository, UUID::randomUUID, LocalDateTime::now);
    }

    @Bean
    public CreateDepositPort depositService(AccountTransactionRecordRepository accountTransactionRecordRepository) {
        return new DepositService(accountTransactionRecordRepository);
    }
}
