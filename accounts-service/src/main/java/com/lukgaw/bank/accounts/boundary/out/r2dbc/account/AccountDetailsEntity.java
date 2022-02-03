package com.lukgaw.bank.accounts.boundary.out.r2dbc.account;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Currency;
import java.util.UUID;

@Getter
@Setter
@Table("ACCOUNT_DETAILS")
public class AccountDetailsEntity {
    @Id
    @NonNull
    private UUID id;
    @NonNull
    private UUID customerOwnerId;
    @NonNull
    private Currency accountCurrency;
    @NonNull
    private String ownerFirstName;
    @NonNull
    private String ownerLastName;
}
