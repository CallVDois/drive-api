package com.callv2.drive.infrastructure.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityContext {

    private SecurityContext() {
    }

    public static String getAuthenticatedUser() {
        return Optional
                .ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .orElseThrow();
    }

}
