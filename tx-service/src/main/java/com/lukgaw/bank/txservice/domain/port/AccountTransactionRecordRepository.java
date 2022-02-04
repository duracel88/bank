package com.lukgaw.bank.txservice.domain.port;

import com.lukgaw.bank.txservice.domain.model.AccountId;
import com.lukgaw.bank.txservice.domain.model.AccountTransactionRecord;
import com.lukgaw.bank.txservice.domain.model.TransactionId;
import reactor.core.publisher.Mono;

public interface AccountTransactionRecordRepository extends FindTransactionsPort {
    Mono<TransactionId> saveTransaction(AccountTransactionRecord accountTransactionRecord);
    Mono<AccountTransactionRecord> findLastAccountTransactionRecord(AccountId accountId);

}
