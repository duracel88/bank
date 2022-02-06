package com.lukgaw.bank.accounts.boundary.in.http.accounts;

import java.math.BigDecimal;

class OpenAccountRequestDTOFixture extends OpenAccountRequestDTO {

    public static OpenAccountRequestDTO createRequest(String firstName, String lastName, BigDecimal firstDeposit, String currency) {
        OpenAccountRequestDTO request = new OpenAccountRequestDTO();
        request.firstName = firstName;
        request.lastName = lastName;
        request.firstDepositAmount = firstDeposit;
        request.currency = currency;
        return request;
    }
}