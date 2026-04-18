package com.codexp.solutions.shared.application;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.codexp.solutions.shared.domain.model.valueobjects.JwtPrincipal;

@Service
public class UserContext {

    public JwtPrincipal getPrincipal() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof JwtPrincipal jwtPrincipal) {
            return jwtPrincipal;
        }

        throw new IllegalStateException("Authenticated principal is not JwtPrincipal");
    }

    public String getUserId() {
        return getPrincipal().userId().value();
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found in security context");
        }

        return authentication;
    }
}
