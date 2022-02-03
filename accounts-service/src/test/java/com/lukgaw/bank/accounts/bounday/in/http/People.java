package com.lukgaw.bank.accounts.bounday.in.http;

import com.lukgaw.bank.accounts.domain.common.UserRole;
import lombok.experimental.UtilityClass;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;

import java.util.UUID;

@UtilityClass
public class People {

    public interface JoeCustomer {
        UUID ID = UUID.fromString("2f52338b-0842-43e7-b454-e85c1ca2d313");
        UserRole ROLE = UserRole.CUSTOMER;
        SecurityMockServerConfigurers.JwtMutator JWT_MUTATOR = SecurityMockServerConfigurers.mockJwt()
                .authorities(ROLE)
                .jwt(builder -> builder.subject(ID.toString()));
    }

    public interface SuseCustomer {
        UUID ID = UUID.fromString("2f52338b-2222-1111-3333-e85c1ca2d313");
        UserRole ROLE = UserRole.CUSTOMER;
        SecurityMockServerConfigurers.JwtMutator JWT_MUTATOR = SecurityMockServerConfigurers.mockJwt()
                .authorities(ROLE)
                .jwt(builder -> builder.subject(ID.toString()));
    }

    public interface DannyAdmin {
        UUID ID = UUID.fromString("2f52338b-5555-1111-3333-e85c1ca2d313");
        UserRole ROLE = UserRole.ADMIN;
        SecurityMockServerConfigurers.JwtMutator JWT_MUTATOR = SecurityMockServerConfigurers.mockJwt()
                .authorities(ROLE)
                .jwt(builder -> builder.subject(ID.toString()));
    }
}
