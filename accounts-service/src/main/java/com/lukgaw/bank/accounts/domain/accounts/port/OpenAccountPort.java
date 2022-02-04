package com.lukgaw.bank.accounts.domain.accounts.port;

import com.lukgaw.bank.accounts.domain.accounts.model.AccountId;
import com.lukgaw.bank.accounts.domain.accounts.model.AccountOwner;
import com.lukgaw.bank.accounts.domain.common.Money;
import lombok.Value;
import reactor.core.publisher.Mono;

import java.util.Currency;

public interface OpenAccountPort {
    Mono<AccountId> openAccount(AccountOwner ownerDetails, Currency accountCurrency);
    Mono<OpenAccountResult> openAccount(AccountOwner ownerDetails, Money firstDeposit);

    @Value(staticConstructor = "of")
    class OpenAccountResult {
        AccountId accountId;
        boolean deposit;
    }

}
