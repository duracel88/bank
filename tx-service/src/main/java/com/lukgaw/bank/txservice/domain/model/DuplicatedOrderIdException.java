package com.lukgaw.bank.txservice.domain.model;

import com.lukgaw.bank.txservice.domain.ex.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class DuplicatedOrderIdException extends BusinessException {

    public DuplicatedOrderIdException() {
        super("Duplicated Order Id");
    }

    public DuplicatedOrderIdException(String message) {
        super(message);
    }

    public DuplicatedOrderIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
