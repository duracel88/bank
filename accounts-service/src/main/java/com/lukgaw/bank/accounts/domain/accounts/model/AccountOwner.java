package com.lukgaw.bank.accounts.domain.accounts.model;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class AccountOwner {
    @NonNull CustomerId customerId;
    @NonNull String firstName;
    @NonNull String lastName;

    public static AccountOwner of(UUID customerId, String firstName, String lastName) {
        return AccountOwner.of(CustomerId.of(customerId), firstName, lastName);
    }
}
