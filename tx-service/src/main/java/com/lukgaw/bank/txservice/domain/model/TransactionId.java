package com.lukgaw.bank.txservice.domain.model;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class TransactionId {
    @NonNull UUID id;
}
