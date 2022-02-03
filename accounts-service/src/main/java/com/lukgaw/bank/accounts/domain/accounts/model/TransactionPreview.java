package com.lukgaw.bank.accounts.domain.accounts.model;

import com.lukgaw.bank.accounts.domain.common.Money;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class TransactionPreview {
    @NonNull UUID transactionId;
    @NonNull Long orderId;
    @NonNull Money transactionAmount;
    @NonNull Money balanceAfter;
}
