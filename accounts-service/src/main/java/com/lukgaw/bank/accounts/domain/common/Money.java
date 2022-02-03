package com.lukgaw.bank.accounts.domain.common;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Money {
    @NonNull BigDecimal value;
    @NonNull Currency currency;

    public static Money zero(@NonNull Currency currency) {
        return Money.of(0D, currency);
    }

    public static Money of(@NonNull double value, @NonNull String currency) {
        return new Money(BigDecimal.valueOf(value), Currency.getInstance(currency));
    }

    public static Money of(@NonNull double value, @NonNull Currency currency) {
        return new Money(BigDecimal.valueOf(value), currency);
    }

    public static Money of(@NonNull BigDecimal value, @NonNull Currency currency) {
        if (value.scale() > 2) {
            throw new IllegalArgumentException("The scale must not be greater than 2, but is " + value.precision());
        }
        return new Money(value, currency);
    }
}
