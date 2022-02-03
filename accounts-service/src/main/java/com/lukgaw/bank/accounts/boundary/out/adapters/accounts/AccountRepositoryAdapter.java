package com.lukgaw.bank.accounts.boundary.out.adapters.accounts;

import com.lukgaw.bank.accounts.boundary.out.http.tx.AccountTransactionsDTO;
import com.lukgaw.bank.accounts.boundary.out.http.tx.TransactionsService;
import com.lukgaw.bank.accounts.boundary.out.r2dbc.account.AccountDetailsEntity;
import com.lukgaw.bank.accounts.boundary.out.r2dbc.account.AccountDetailsReactiveCrudRepository;
import com.lukgaw.bank.accounts.domain.accounts.model.Account;
import com.lukgaw.bank.accounts.domain.accounts.model.AccountId;
import com.lukgaw.bank.accounts.domain.accounts.model.AccountOwner;
import com.lukgaw.bank.accounts.domain.accounts.model.TransactionPreview;
import com.lukgaw.bank.accounts.domain.accounts.port.AccountRepository;
import com.lukgaw.bank.accounts.domain.common.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepository {
    private final AccountDetailsReactiveCrudRepository accountDetailsRepository;
    private final TransactionsService transactionsService;

    @Override
    public Mono<Account> findAccountById(AccountId accountId, int lastTransactionCount) {
        var accountDetailsMono = accountDetailsRepository.findById(accountId.getId());
        var lastAccountTransactionsMono = transactionsService
                .getLastAccountTransactions(accountId.getId(), lastTransactionCount)
                .map(AccountTransactionsDTO::getTransactions)
                .map(Page::toList)
                .defaultIfEmpty(List.of());

        return Mono.zip(accountDetailsMono, lastAccountTransactionsMono)
                .map(tuple -> createDomainObject(tuple.getT1(), tuple.getT2()));
    }

    private Account createDomainObject(AccountDetailsEntity accountDetailsEntity, List<AccountTransactionsDTO.TransactionDTO> accountTransactionsDTO) {
        var accountOwner = AccountOwner.of(
                accountDetailsEntity.getId(),
                accountDetailsEntity.getOwnerFirstName(),
                accountDetailsEntity.getOwnerLastName());
        var transactions = accountTransactionsDTO.stream()
                .map(this::createTransactionPreview)
                .sorted(Comparator.comparing(TransactionPreview::getOrderId).reversed())
                .collect(Collectors.toList());
        if (transactions.isEmpty()) {
            return Account.emptyBalanceAccountBuilder()
                    .accountId(AccountId.of(accountDetailsEntity.getId()))
                    .ownerDetails(accountOwner)
                    .currency(accountDetailsEntity.getAccountCurrency())
                    .build();
        } else {
            Money currentBalance = transactions.stream().findFirst().map(TransactionPreview::getBalanceAfter).orElseThrow();
            return Account.accountBuilder()
                    .accountId(AccountId.of(accountDetailsEntity.getId()))
                    .ownerDetails(accountOwner)
                    .transactions(transactions)
                    .balance(currentBalance)
                    .build();
        }
    }

    private TransactionPreview createTransactionPreview(AccountTransactionsDTO.TransactionDTO transaction) {
        return TransactionPreview.builder()
                .transactionId(transaction.getId())
                .orderId(transaction.getOrderId())
                .balanceAfter(Money.of(transaction.getAmountAfter(), transaction.getCurrency()))
                .transactionAmount(Money.of(transaction.getTransactionAmount(), transaction.getCurrency()))
                .build();
    }
}
