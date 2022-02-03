package com.lukgaw.bank.accounts.domain.accounts.model;

import com.lukgaw.bank.accounts.domain.common.Money;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Currency;
import java.util.List;

@Getter
public class Account {
    private final AccountId accountId;
    private final AccountOwner ownerDetails;
    private Money balance;
    private List<TransactionPreview> transactions;

    @Builder(builderClassName = "EmptyBalanceAccountBuilder", builderMethodName = "emptyBalanceAccountBuilder")
    private Account(@NonNull AccountId accountId,
                    @NonNull AccountOwner ownerDetails,
                    @NonNull Currency currency) {
        this.accountId = accountId;
        this.ownerDetails = ownerDetails;
        this.balance = Money.zero(currency);
        this.transactions = List.of();
    }

    @Builder(builderClassName = "AccountBuilder", builderMethodName = "accountBuilder")
    private Account(@NonNull AccountId accountId,
                    @NonNull AccountOwner ownerDetails,
                    @NonNull Money balance,
                    @NonNull List<TransactionPreview> transactions) {
        this.accountId = accountId;
        this.ownerDetails = ownerDetails;
        this.balance = balance;
        this.transactions = List.copyOf(transactions);
    }

}
