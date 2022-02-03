package com.lukgaw.bank.accounts.domain.common;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ADMIN, CUSTOMER;

    @Override
    public String getAuthority() {
        return "ROLE_" + this;
    }
}
