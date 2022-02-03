package com.lukgaw.bank.accounts.boundary.in.http.accounts;

import com.lukgaw.bank.accounts.domain.accounts.model.Account;
import com.lukgaw.bank.accounts.domain.accounts.model.AccountOwner;
import com.lukgaw.bank.accounts.domain.accounts.model.TransactionPreview;

import java.util.List;
import java.util.stream.Collectors;

class AccountDTOMapper {

    AccountDTO map(Account account) {
        AccountOwner ownerDetails = account.getOwnerDetails();
        List<TransactionPreview> transactions = account.getTransactions();
        return AccountDTO.builder()
                .accountId(account.getAccountId().getId())
                .customerId(ownerDetails.getCustomerId().getId())
                .firstName(ownerDetails.getFirstName())
                .lastName(ownerDetails.getLastName())
                .balance(account.getBalance().getValue())
                .currency(account.getBalance().getCurrency().getCurrencyCode())
                .transactions(transactions.stream().map(this::map).collect(Collectors.toList()))
                .build();
    }

    private AccountDTO.TransactionDTO map(TransactionPreview transaction) {
        return AccountDTO.TransactionDTO.builder()
                .id(transaction.getTransactionId())
                .amount(transaction.getTransactionAmount().getValue())
                .orderId(transaction.getOrderId())
                .build();
    }
}
