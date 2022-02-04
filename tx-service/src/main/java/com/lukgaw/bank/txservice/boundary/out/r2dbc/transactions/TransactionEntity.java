package com.lukgaw.bank.txservice.boundary.out.r2dbc.transactions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

@Builder
@Getter
@Setter
@Table("TRANSACTIONS")
public class TransactionEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    private UUID transactionCorrelationId;
    private UUID accountId;
    private Long orderId;
    private Boolean isDeposit;
    private Currency currency;
    private BigDecimal transactionAmount;
    private BigDecimal amountAfter;
    private LocalDateTime timestamp;

    @Override
    public boolean isNew() {
        return true;
    }

    public Optional<UUID> getTransactionCorrelationId() {
        return Optional.ofNullable(transactionCorrelationId);
    }
}
