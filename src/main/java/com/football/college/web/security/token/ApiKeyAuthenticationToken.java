package com.football.college.web.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiKeyAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public ApiKeyAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public ApiKeyAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}