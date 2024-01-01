package com.project.workflow.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class AdminAuthenticationProvider implements AuthenticationProvider {
    private final String adminEmail;
    private final String adminPassword;

    public AdminAuthenticationProvider(String adminEmail, String adminPassword) {
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // validation of admin credentials compared to application.properties
        if (adminEmail.equals(username) && adminPassword.equals(password)) {
            return new UsernamePasswordAuthenticationToken(username, password, authentication.getAuthorities());
        }

        // if credentials are not valid= authentication failure
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
