package com.lukgaw.bank.accounts.boundary.in.http.security;

import com.lukgaw.bank.accounts.domain.accounts.model.CustomerId;
import com.lukgaw.bank.accounts.domain.common.UserRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationTools {

    public static boolean isAdmin(JwtAuthenticationToken authentication) {
        return authentication.getAuthorities()
                .contains(UserRole.ADMIN);
    }

    public static CustomerId getCustomerId(JwtAuthenticationToken authentication) {
        return CustomerId.of(UUID.fromString(authentication.getName()));
    }
}
