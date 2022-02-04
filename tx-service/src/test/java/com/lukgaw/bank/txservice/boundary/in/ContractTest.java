package com.lukgaw.bank.txservice.boundary.in;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Import(ContractTest.WebFluxControllerSecurityTestConfig.class)
public abstract class ContractTest {

    @TestConfiguration
    @EnableReactiveMethodSecurity
    public static class WebFluxControllerSecurityTestConfig {

        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
            return http
                    .authorizeExchange(x -> x.anyExchange().authenticated())
                    .csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .build();
        }
    }
}
