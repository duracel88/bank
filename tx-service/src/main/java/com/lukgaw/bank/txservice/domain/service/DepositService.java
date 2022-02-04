package com.lukgaw.bank.txservice.domain.service;

import com.lukgaw.bank.txservice.domain.model.AccountId;
import com.lukgaw.bank.txservice.domain.model.AccountTransactionRecord;
import com.lukgaw.bank.txservice.domain.model.DuplicatedOrderIdException;
import com.lukgaw.bank.txservice.domain.model.Money;
import com.lukgaw.bank.txservice.domain.port.AccountTransactionRecordRepository;
import com.lukgaw.bank.txservice.domain.port.CreateDepositPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DepositService implements CreateDepositPort {
    private final AccountTransactionRecordRepository transactionRepository;

    @Override
    public Mono<DepositResult> deposit(@NonNull AccountId targetAccountId, @NonNull Money money) {
        if (!money.isPositive()) {
            return Mono.just(DepositResult.failedDeposit("Deposit's amount must be positive"));
        }
        return transactionRepository.findLastAccountTransactionRecord(targetAccountId)
                .map(lastTransaction -> followLastTransaction(targetAccountId, money, lastTransaction))
                .defaultIfEmpty(firstDeposit(targetAccountId, money))
                .flatMap(transactionRepository::saveTransaction)
                .map(DepositResult::successfulDeposit)
                .onErrorResume(DuplicatedOrderIdException.class, ex -> Mono.just(DepositResult.duplicatedOrderId()))
                .onErrorResume(ex -> Mono.just(DepositResult.failedDeposit("Deposit couldn't be saved ", ex.getMessage())));
    }

    private AccountTransactionRecord followLastTransaction(AccountId accountId, Money transactionAmount, AccountTransactionRecord lastTransaction) {
        return AccountTransactionRecord.newDeposit()
                .accountId(accountId)
                .transferAmount(transactionAmount)
                .amountAfter(lastTransaction.getAmountAfter().sum(transactionAmount))
                .orderId(lastTransaction.getOrderId() + 1)
                .build();
    }

    private AccountTransactionRecord firstDeposit(AccountId accountId, Money transactionAmount) {
        return AccountTransactionRecord.newDeposit()
                .accountId(accountId)
                .transferAmount(transactionAmount)
                .amountAfter(transactionAmount)
                .orderId(1L)
                .build();
    }

}
