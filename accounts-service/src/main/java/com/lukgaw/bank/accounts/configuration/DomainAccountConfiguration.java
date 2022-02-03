package com.lukgaw.bank.accounts.configuration;

import com.lukgaw.bank.accounts.boundary.out.adapters.accounts.AccountRepositoryAdapter;
import com.lukgaw.bank.accounts.boundary.out.http.tx.TransactionsService;
import com.lukgaw.bank.accounts.boundary.out.r2dbc.account.AccountDetailsReactiveCrudRepository;
import com.lukgaw.bank.accounts.domain.accounts.port.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainAccountConfiguration {

    @Bean
    public AccountRepository accountRepository(AccountDetailsReactiveCrudRepository accountDetailsRepository,
                                               TransactionsService transactionsService) {
        return new AccountRepositoryAdapter(accountDetailsRepository, transactionsService);
    }
}
