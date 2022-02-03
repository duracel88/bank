package com.lukgaw.bank.accounts.domain.accounts.model;

import com.lukgaw.bank.accounts.domain.common.Money;

import java.util.List;
import java.util.UUID;

class AccountFixture {

    public static Account createAccount(AccountId accountId) {
        return Account.accountBuilder()
                .accountId(accountId)
                .balance(Money.of(20.00D, "PLN"))
                .transactions(List.of())
                .ownerDetails(AccountOwner.of(UUID.randomUUID(), "Luk", "Gaw"))
                .build();
    }
}