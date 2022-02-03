package com.lukgaw.bank.accounts.boundary.out.r2dbc.account;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountDetailsReactiveCrudRepository extends ReactiveCrudRepository<AccountDetailsEntity, UUID> {
}
