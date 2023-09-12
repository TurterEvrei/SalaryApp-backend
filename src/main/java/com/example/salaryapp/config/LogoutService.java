package com.example.salaryapp.config;

import com.example.salaryapp.entities.Token;
import com.example.salaryapp.entities.User;
import com.example.salaryapp.repositories.TokenRepo;
import com.example.salaryapp.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepo tokenRepo;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String jwt = authHeader.substring(7);
            User user = (User) userDetailsService.loadUserByUsername(
                    jwtService.extractUsername(jwt)
            );
            List<Token> storedTokens = tokenRepo.findAllValidTokenByUserId(user.getId());
            if (!storedTokens.isEmpty()) {
                storedTokens.forEach(token -> {
                    token.setExpired(true);
                    token.setRevoked(true);
                });
                tokenRepo.saveAll(storedTokens);
                SecurityContextHolder.clearContext();
            }
        }
    }
}
