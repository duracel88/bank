package com.lukgaw.bank.accounts.domain.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTest {

    @Test
    public void the_scale_must_not_be_greater_than_2() {
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> Money.of(BigDecimal.valueOf(22.222D), Currency.getInstance("PLN")));
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> Money.of(BigDecimal.valueOf(22.22233D), Currency.getInstance("PLN")));
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> Money.of(BigDecimal.valueOf(22.202D), Currency.getInstance("PLN")));
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> Money.of(BigDecimal.valueOf(0.202D), Currency.getInstance("PLN")));
    }

    @Test
    public void the_value_can_be_negative() {
        //given
        var money = Money.of(BigDecimal.valueOf(-22.22D), Currency.getInstance("PLN"));

        //expect
        assertEquals(-22.22D, money.getValue().doubleValue());
    }

    @Test
    public void the_value_can_not_be_null(){
        Assertions.assertThrowsExactly(NullPointerException.class, () -> Money.of(null, Currency.getInstance("PLN")));
    }

    @Test
    public void the_currency_can_not_be_null(){
        Assertions.assertThrowsExactly(NullPointerException.class, () -> Money.of(BigDecimal.ONE, null));
    }
}