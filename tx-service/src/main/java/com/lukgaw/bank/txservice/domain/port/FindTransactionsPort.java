package com.lukgaw.bank.txservice.domain.port;

import com.lukgaw.bank.txservice.domain.model.AccountId;
import com.lukgaw.bank.txservice.domain.model.AccountTransactionRecord;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public interface FindTransactionsPort {
    Flux<AccountTransactionRecord> findAccountTransactionRecords(AccountId accountId, Pageable pageable);

}
