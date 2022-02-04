package com.lukgaw.bank.txservice.boundary.in;

import com.lukgaw.bank.txservice.domain.model.AccountId;
import com.lukgaw.bank.txservice.domain.model.Money;
import com.lukgaw.bank.txservice.domain.model.TransactionId;
import com.lukgaw.bank.txservice.domain.port.CreateDepositPort;
import com.lukgaw.bank.txservice.domain.port.CreateDepositPort.DepositResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deposit")
@Slf4j
public class DepositController {
    private final CreateDepositPort createDepositPort;

    @PostMapping
    public Mono<ResponseEntity<?>> deposit(@Valid @RequestBody DepositRequest depositRequest) {
        AccountId accountId = AccountId.of(depositRequest.getTargetAccountId());
        Money deposit = Money.of(depositRequest.getDepositAmount(), Currency.getInstance(depositRequest.getCurrency()));
        return createDepositPort.deposit(accountId, deposit)
                .map(this::toResponseEntity);
    }

    private ResponseEntity<?> toResponseEntity(DepositResult depositResult) {
        if (depositResult.succeeded()) {
            return ResponseEntity.of(depositResult.getSuccessfulDepositTransactionId()
                    .map(TransactionId::getId)
                    .map(DepositSucceededResponse::new));
        } else if(depositResult.isFailedOnOptimisticLocking()){
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(depositResult.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(depositResult.getMessage());
        }
    }

    @Getter
    public static final class DepositRequest {
        @NotNull
        UUID targetAccountId;
        @NotNull
        @Positive
        BigDecimal depositAmount;
        @NotNull
        @Pattern(regexp = "[A-Z]{3}")
        String currency;
    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    public static final class DepositSucceededResponse {
        private UUID transactionId;
    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    public static final class DepositFailedResponse {
        private String message;
    }
}
