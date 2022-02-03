package com.lukgaw.bank.accounts.boundary.out.r2dbc;

import com.lukgaw.bank.accounts.domain.accounts.model.AccountId;
import com.lukgaw.bank.accounts.domain.accounts.model.CustomerId;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class DatabaseAccounts {
    public static final AccountId CHUCK_ACCOUNT_ID = AccountId.of(UUID.fromString("132d61c1-12f2-4b70-9079-c6ff34903bbb"));
    public static final CustomerId CHUCK_CUSTOMER_ID = CustomerId.of(UUID.fromString("132d61c1-12f2-4b70-9079-c6ff34903bbb"));
    public static final String CHUCK_NAME = "Chuck";
    public static final String CHUCK_LASTNAME = "Norris";

    public static final AccountId SPIDERMAN_ACCOUNT_ID = AccountId.of(UUID.fromString("132d61c1-12f2-4b70-9079-c6ff34903bbc"));
    public static final CustomerId SPIDERMAN_CUSTOMER_ID = CustomerId.of(UUID.fromString("132d61c1-12f2-4b70-9079-c6ff34903bbc"));
    public static final String SPIDERMAN_NAME = "Spider";
    public static final String SPIDERMAN_LASTNAME = "Man";

}
