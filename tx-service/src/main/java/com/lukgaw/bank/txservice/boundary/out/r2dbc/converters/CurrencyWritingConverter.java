package com.lukgaw.bank.txservice.boundary.out.r2dbc.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.util.Currency;

@WritingConverter
@Component
public class CurrencyWritingConverter implements Converter<Currency, String> {

    @Override
    public String convert(Currency source) {
        return source.getCurrencyCode();
    }
}
