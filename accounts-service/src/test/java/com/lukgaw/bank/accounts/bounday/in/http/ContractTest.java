package com.lukgaw.bank.accounts.bounday.in.http;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;

@Import(ContractTest.WebFluxControllerSecurityTestConfig.class)
public abstract class ContractTest {

    @TestConfiguration
    @EnableReactiveMethodSecurity
    public static class WebFluxControllerSecurityTestConfig {
    }
}
