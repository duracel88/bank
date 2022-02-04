package com.lukgaw.bank.txservice.boundary.out.r2dbc.transactions;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface TransactionReactiveCrudRepository extends ReactiveCrudRepository<TransactionEntity, UUID> {
    Flux<TransactionEntity> findByAccountIdOrderByOrderIdDesc(UUID accountId, Pageable pageable);
}
