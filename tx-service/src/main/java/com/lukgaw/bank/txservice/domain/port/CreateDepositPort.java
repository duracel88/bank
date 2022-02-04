package com.lukgaw.bank.txservice.domain.port;

import com.lukgaw.bank.txservice.domain.model.AccountId;
import com.lukgaw.bank.txservice.domain.model.Money;
import com.lukgaw.bank.txservice.domain.model.TransactionId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface CreateDepositPort {
    Mono<DepositResult> deposit(AccountId accountId, Money deposit);

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    class DepositResult {
        @NonNull private final boolean succeeded;
        private final TransactionId successfulDepositTransactionId;
        @NonNull @Getter private final String message;
        @NonNull @Getter private final String detailedMessage;
        @Getter private final boolean failedOnOptimisticLocking;

        public static DepositResult successfulDeposit(@NonNull TransactionId transactionId) {
            return new DepositResult(true, transactionId, "Created", "Created", false);
        }

        public static DepositResult failedDeposit(@NonNull String message) {
            return new DepositResult(false, null, message, message, false);
        }

        public static DepositResult duplicatedOrderId() {
            return new DepositResult(false, null, "Duplicated orderId", "Duplicated orderId", true);
        }

        public static DepositResult failedDeposit(@NonNull String message, @NonNull String detailedMessage) {
            return new DepositResult(false, null, message, detailedMessage, false);
        }

        public Boolean succeeded() {
            return succeeded;
        }

        public Optional<TransactionId> getSuccessfulDepositTransactionId() {
            return Optional.ofNullable(successfulDepositTransactionId);
        }
    }
}
