package com.lukgaw.bank.accounts.boundary.out.r2dbc.account;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Currency;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table("ACCOUNT_DETAILS")
public class AccountDetailsEntity implements Persistable<UUID> {
    @Id
    @NonNull private UUID id;
    @NonNull private UUID customerOwnerId;
    @NonNull private Currency accountCurrency;
    @NonNull private String ownerFirstName;
    @NonNull private String ownerLastName;

    @Override
    public boolean isNew() {
        return true;
    }
}
