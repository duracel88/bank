package com.lukgaw.bank.accounts.boundary.in.http.security;

import com.lukgaw.bank.accounts.domain.common.UserRole;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GrantedAuthoritiesRealmRolesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {
    private final Map<String, UserRole> realmRolesMap;

    public Collection<GrantedAuthority> convert(Jwt jwt) {
        JSONObject realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) {
            return Set.of();
        }
        JSONArray roles = (JSONArray) realmAccess.getOrDefault("roles", new JSONArray());
        return Arrays.stream(roles.toArray())
                .map(Object::toString)
                .map(realmRolesMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
