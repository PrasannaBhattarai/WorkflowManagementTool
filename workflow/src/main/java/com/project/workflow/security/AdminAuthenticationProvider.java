package com.project.workflow.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;

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
            System.out.println("HIIIIII"+authentication.isAuthenticated()+" lol "+ authentication.getAuthorities() );
            // authentication.getAuthorities()
            return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
        }

        // if credentials are not valid= authentication failure
        throw new BadCredentialsException("Invalid admin credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
