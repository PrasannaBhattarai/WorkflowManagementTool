package com.project.workflow.service;

import com.project.workflow.models.AuthenticationRequest;
import com.project.workflow.models.AuthenticationResponse;
import com.project.workflow.models.RegisterBody;
import com.project.workflow.models.User;
import com.project.workflow.repository.UserRepository;
import com.project.workflow.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    public AuthenticationResponse register(RegisterBody registerBody) {
        var user = User.builder()
                .firstName(registerBody.getFirstName())
                .lastName(registerBody.getLastName())
                .email(registerBody.getEmail())
                .password(passwordEncoder.encode(registerBody.getPassword()))
                .userName(registerBody.getUserName())
                .userRatings(0)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(RegisterBody registerBody) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerBody.getEmail(),
                        registerBody.getPassword()
                )
        );
        var user = userRepository.findByEmail(registerBody.getEmail())
                .orElseThrow(Exception::new);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse adminAuthenticate(RegisterBody registerBody) {
        try {

            // Validation of credentials and admin properties
            if (adminEmail.equals(registerBody.getEmail()) && adminPassword.equals(registerBody.getPassword())) {
                // Generate JWT token for the admin user
                String jwtToken = jwtService.generateTokenForAdmin();

                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .build();
            } else {
                throw new RuntimeException("Invalid admin credentials");
            }
        } catch (Exception e) {
            // Handle authentication failure
            throw new RuntimeException("Admin authentication failed", e);
        }
    }

}
