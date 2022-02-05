package com.lukgaw.bank.accounts.configuration;

import com.lukgaw.bank.accounts.boundary.in.http.security.GrantedAuthoritiesRealmRolesExtractor;
import com.lukgaw.bank.accounts.domain.common.UserRole;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Component;

import java.util.Map;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class JWTSecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         ReactiveJwtAuthenticationConverterAdapter authenticationConverter,
                                                         ReactiveJwtDecoder reactiveJwtDecoder) {
        return http
                .authorizeExchange(ex -> ex.anyExchange().authenticated())
                .oauth2ResourceServer(oAuth -> oAuth
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(authenticationConverter)
                                .jwtDecoder(reactiveJwtDecoder)))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverterAdapter authenticationConverter(RealmRolesMap realmRolesMap) {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new GrantedAuthoritiesRealmRolesExtractor(realmRolesMap.getRealmAccessRolesMap()));
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }


    @Data
    @Component
    @ConfigurationProperties(prefix = "jwt")
    public static class RealmRolesMap {
        private Map<String, UserRole> realmAccessRolesMap;
    }

}
