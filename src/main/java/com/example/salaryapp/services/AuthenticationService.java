package com.example.salaryapp.services;

import com.example.salaryapp.auth.AuthRequest;
import com.example.salaryapp.auth.AuthResponse;
import com.example.salaryapp.auth.RegisterRequest;
import com.example.salaryapp.entities.Token;
import com.example.salaryapp.entities.User;
import com.example.salaryapp.entities.enums.Role;
import com.example.salaryapp.entities.enums.TokenType;
import com.example.salaryapp.repositories.TokenRepo;
import com.example.salaryapp.repositories.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenRepo tokenRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;

    public AuthResponse registration(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .roles(Set.of(Role.USER))
                .active(true)
                .build();
        userRepo.save(user);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(refreshToken, user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse authentication(AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        System.out.println(request);
        User user = (User) userDetailsService.loadUserByUsername(request.getEmail());
        revokeAllUserTokens(user);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(refreshToken, user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse refresh(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String email = jwtService.extractUsername(jwt);
            if (email != null) {
                User user = (User) userDetailsService.loadUserByUsername(email);
                boolean isTokenValid = tokenRepo.findByToken(jwt)
                        .map(token -> !token.isExpired() && !token.isRevoked())
                        .orElse(false);
                if (isTokenValid && jwtService.isTokenValid(jwt, user)) {
                    revokeAllUserTokens(user);
                    String refreshToken = jwtService.generateRefreshToken(user);
                    saveUserToken(refreshToken, user);
                    return AuthResponse.builder()
                            .accessToken(jwtService.generateAccessToken(user))
                            .refreshToken(refreshToken)
                            .build();
                }
            }
        }
        return null;
    }

    private void saveUserToken(String jwt, User user) {
        Token token = Token.builder()
                .token(jwt)
                .user(user)
                .revoked(false)
                .expired(false)
                .tokenType(TokenType.BEARER)
                .build();
        tokenRepo.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> tokens = tokenRepo.findAllValidTokenByUserId(user.getId());
        if (tokens.isEmpty()) {
            return;
        }
        tokens.forEach((token) -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepo.saveAll(tokens);
    }

}
