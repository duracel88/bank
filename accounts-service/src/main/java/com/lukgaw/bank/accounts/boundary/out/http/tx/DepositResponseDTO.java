package com.lukgaw.bank.accounts.boundary.out.http.tx;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DepositResponseDTO {
    private UUID transactionId;
}
