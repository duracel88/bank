package com.lukgaw.bank.txservice.boundary.out.adapters;

import com.lukgaw.bank.txservice.boundary.out.r2dbc.transactions.TransactionEntity;
import com.lukgaw.bank.txservice.boundary.out.r2dbc.transactions.TransactionReactiveCrudRepository;
import com.lukgaw.bank.txservice.domain.model.*;
import com.lukgaw.bank.txservice.domain.port.AccountTransactionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class AccountTransactionRecordRepositoryAdapter implements AccountTransactionRecordRepository {
    private final TransactionReactiveCrudRepository reactiveCrudRepository;
    private final Supplier<UUID> uuidGenerator;
    private final Supplier<LocalDateTime> timestampProvider;

    @Override
    public Mono<TransactionId> saveTransaction(AccountTransactionRecord accountTransactionRecord) {
        return reactiveCrudRepository.save(TransactionEntity.builder()
                        .id(uuidGenerator.get())
                        .transactionCorrelationId(accountTransactionRecord.getTransactionCorrelationId()
                                .map(TransactionCorrelationId::getId)
                                .orElse(null))
                        .accountId(accountTransactionRecord.getAccountId().getId())
                        .orderId(accountTransactionRecord.getOrderId())
                        .isDeposit(accountTransactionRecord.isDeposit())
                        .currency(accountTransactionRecord.getTransferAmount().getCurrency())
                        .transactionAmount(accountTransactionRecord.getTransferAmount().getValue())
                        .amountAfter(accountTransactionRecord.getAmountAfter().getValue())
                        .timestamp(timestampProvider.get())
                        .build())
                .onErrorResume(DataIntegrityViolationException.class, ex -> Mono.error(() -> new DuplicatedOrderIdException("Data Integrity Violation", ex)))
                .map(TransactionEntity::getId)
                .map(TransactionId::of);
    }

    @Override
    public Flux<AccountTransactionRecord> findAccountTransactionRecords(AccountId accountId, Pageable pageable) {
        return reactiveCrudRepository.findByAccountIdOrderByOrderIdDesc(accountId.getId(), pageable)
                .map(entity ->
                        AccountTransactionRecord.builder()
                                .transactionId(TransactionId.of(entity.getId()))
                                .transactionCorrelationId(entity.getTransactionCorrelationId().map(TransactionCorrelationId::of).orElse(null))
                                .accountId(accountId)
                                .amountAfter(Money.of(entity.getAmountAfter(), entity.getCurrency()))
                                .transferAmount(Money.of(entity.getTransactionAmount(), entity.getCurrency()))
                                .orderId(entity.getOrderId())
                                .build()
                );
    }

    @Override
    public Mono<AccountTransactionRecord> findLastAccountTransactionRecord(AccountId accountId) {
        return findAccountTransactionRecords(accountId, PageRequest.of(0, 1))
                .take(1)
                .single();
    }
}
