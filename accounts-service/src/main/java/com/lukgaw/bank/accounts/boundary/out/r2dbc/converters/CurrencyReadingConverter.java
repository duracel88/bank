package com.lukgaw.bank.accounts.boundary.out.r2dbc.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.util.Currency;

@ReadingConverter
@Component
public class CurrencyReadingConverter implements Converter<String, Currency> {
    @Override
    public Currency convert(String source) {
        return Currency.getInstance(source);
    }
}
