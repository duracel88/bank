package com.lukgaw.bank.accounts.boundary.in.http.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAuthorizedResourceAccessException extends RuntimeException {
}
