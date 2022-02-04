package com.lukgaw.bank.txservice.boundary.in;

import com.lukgaw.bank.txservice.domain.model.AccountId;
import com.lukgaw.bank.txservice.domain.model.AccountTransactionRecord;
import com.lukgaw.bank.txservice.domain.port.FindTransactionsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionResource {
    private final FindTransactionsPort findTransactionsPort;

    @GetMapping("/{accountId}")
    public Mono<ResponseEntity<AccountTransactionsDTO>> getAccountTransactions(@PathVariable UUID accountId,
                                                                               Pageable pageRequest) {
        return findTransactionsPort.findAccountTransactionRecords(AccountId.of(accountId), pageRequest)
                .collectList()
                .map(list -> toDTO(accountId, list))
                .map(ResponseEntity::ok);
    }

    private AccountTransactionsDTO toDTO(UUID accountId, List<AccountTransactionRecord> accountTransactionRecords) {
        return AccountTransactionsDTO.builder()
                .accountId(accountId)
                .transactions(accountTransactionRecords.stream()
                        .map(r -> AccountTransactionsDTO.TransactionDTO.builder()
                                .id(r.getTransactionId().getId())
                                .amountAfter(r.getAmountAfter().getValue())
                                .currency(r.getAmountAfter().getCurrency())
                                .transactionAmount(r.getTransferAmount().getValue())
                                .isDeposit(r.isDeposit())
                                .orderId(r.getOrderId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
