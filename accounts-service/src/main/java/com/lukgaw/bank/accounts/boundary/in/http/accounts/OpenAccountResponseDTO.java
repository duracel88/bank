package com.lukgaw.bank.accounts.boundary.in.http.accounts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
@AllArgsConstructor(staticName = "of")
public class OpenAccountResponseDTO {
    private UUID newAccountId;
    private boolean firstDeposit;

    public static OpenAccountResponseDTO justAccount(@NonNull UUID newAccountId){
        return new OpenAccountResponseDTO(newAccountId, false);
    }
}
