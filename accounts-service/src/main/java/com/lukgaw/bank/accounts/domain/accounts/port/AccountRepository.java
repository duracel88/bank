package com.lukgaw.bank.accounts.domain.accounts.port;

import com.lukgaw.bank.accounts.domain.accounts.model.Account;
import com.lukgaw.bank.accounts.domain.accounts.model.AccountId;
import reactor.core.publisher.Mono;

public interface AccountRepository {
    Mono<Account> findAccountById(AccountId accountId, int lastTransactionCount);
}
