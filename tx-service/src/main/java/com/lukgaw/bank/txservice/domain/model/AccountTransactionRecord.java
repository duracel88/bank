package com.lukgaw.bank.txservice.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Optional;

@Value
public class AccountTransactionRecord {
    TransactionId transactionId;
    TransactionCorrelationId transactionCorrelationId;
    AccountId accountId;
    Money amountAfter;
    Money transferAmount;
    Long orderId;

    @Builder(builderClassName = "PrototypeDepositBuilder", builderMethodName = "newDeposit")
    public AccountTransactionRecord(@NonNull AccountId accountId,
                                    @NonNull Money amountAfter,
                                    @NonNull Money transferAmount,
                                    @NonNull Long orderId) {
        this.transactionId = null;
        this.transactionCorrelationId = null;
        this.accountId = accountId;
        this.amountAfter = amountAfter;
        this.transferAmount = transferAmount;
        this.orderId = orderId;
    }

    @Builder(builderClassName = "PrototypeP2PTransferBuilder", builderMethodName = "newP2PTransfer")
    public AccountTransactionRecord(@NonNull TransactionCorrelationId transactionCorrelationId,
                                    @NonNull AccountId accountId,
                                    @NonNull Money amountAfter,
                                    @NonNull Money transferAmount,
                                    @NonNull Long orderId) {
        this.transactionId = null;
        this.transactionCorrelationId = transactionCorrelationId;
        this.accountId = accountId;
        this.amountAfter = amountAfter;
        this.transferAmount = transferAmount;
        this.orderId = orderId;
    }

    @Builder
    public AccountTransactionRecord(TransactionId transactionId,
                                    TransactionCorrelationId transactionCorrelationId,
                                    AccountId accountId,
                                    Money amountAfter,
                                    Money transferAmount,
                                    Long orderId) {
        this.transactionId = transactionId;
        this.transactionCorrelationId = transactionCorrelationId;
        this.accountId = accountId;
        this.amountAfter = amountAfter;
        this.transferAmount = transferAmount;
        this.orderId = orderId;
    }

    public boolean isDeposit() {
        return transactionCorrelationId == null;
    }

    public Optional<TransactionCorrelationId> getTransactionCorrelationId() {
        return Optional.ofNullable(transactionCorrelationId);
    }
}
