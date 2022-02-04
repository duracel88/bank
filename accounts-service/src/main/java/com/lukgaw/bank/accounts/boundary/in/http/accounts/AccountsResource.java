package com.lukgaw.bank.accounts.boundary.in.http.accounts;

import com.lukgaw.bank.accounts.boundary.in.http.ex.NotAuthorizedResourceAccessException;
import com.lukgaw.bank.accounts.boundary.in.http.ex.ResourceNotFoundException;
import com.lukgaw.bank.accounts.domain.accounts.model.AccountId;
import com.lukgaw.bank.accounts.domain.accounts.model.AccountOwner;
import com.lukgaw.bank.accounts.domain.accounts.model.CustomerId;
import com.lukgaw.bank.accounts.domain.accounts.port.AccountRepository;
import com.lukgaw.bank.accounts.domain.accounts.port.OpenAccountPort;
import com.lukgaw.bank.accounts.domain.common.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Currency;
import java.util.UUID;

import static com.lukgaw.bank.accounts.boundary.in.http.security.AuthenticationTools.getCustomerId;
import static com.lukgaw.bank.accounts.boundary.in.http.security.AuthenticationTools.isAdmin;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountsResource {
    private final static Integer DEFAULT_TRANSACTIONS_COUNT = 50;
    private final AccountDTOMapper accountDTOMapper = new AccountDTOMapper();
    private final AccountRepository accountRepository;
    private final OpenAccountPort openAccountPort;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_ADMIN')")
    public Mono<ResponseEntity<AccountDTO>> getAccountWithMax50Transactions(@PathVariable UUID id,
                                                                            @AuthenticationPrincipal JwtAuthenticationToken auth) {
        AccountId accountId = AccountId.of(id);
        return accountRepository.findAccountById(accountId, DEFAULT_TRANSACTIONS_COUNT)
                .switchIfEmpty(Mono.error(ResourceNotFoundException::new))
                .filter(account -> isAdmin(auth) || account.getOwnerDetails().getCustomerId().equals(getCustomerId(auth)))
                .switchIfEmpty(Mono.error(NotAuthorizedResourceAccessException::new))
                .map(accountDTOMapper::map)
                .map(ResponseEntity::ok);
    }

    @GetMapping(value = "/{id}", params = "size")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Mono<ResponseEntity<AccountDTO>> getAccountWithTransactions(@PathVariable UUID id,
                                                                       @RequestParam int size) {
        AccountId accountId = AccountId.of(id);
        return accountRepository.findAccountById(accountId, size)
                .switchIfEmpty(Mono.error(ResourceNotFoundException::new))
                .map(accountDTOMapper::map)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
    public Mono<ResponseEntity<?>> createAccount(@RequestBody @Valid OpenAccountRequestDTO request,
                                                @AuthenticationPrincipal JwtAuthenticationToken auth) {
        if (!getCustomerId(auth).getId().equals(request.getCustomerId())) {
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        }
        AccountOwner owner = AccountOwner.of(CustomerId.of(request.getCustomerId()), request.getFirstName(), request.getLastName());
        Currency currency = Currency.getInstance(request.getCurrency());
        if (request.getFirstDepositAmount() == null) {
            return openAccountPort.openAccount(owner, currency)
                    .map(accountId -> ResponseEntity.ok(OpenAccountResponseDTO.justAccount(accountId.getId())));
        } else {
            return openAccountPort.openAccount(owner, Money.of(request.getFirstDepositAmount(), currency))
                    .map(result -> OpenAccountResponseDTO.of(result.getAccountId().getId(), result.isDeposit()))
                    .map(ResponseEntity::ok);
        }
    }

}
