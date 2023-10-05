package com.example.salaryapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authProvider;
    private final JwtAuthFilter jwtAuthFilter;
    private final LogoutHandler logoutHandler;


    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**", "/test**")
                    .permitAll()
                .requestMatchers("/api/v1/admin**")
                    .hasAuthority("Admin")
                .requestMatchers("/api/v1/manager**")
                    .hasAnyAuthority("Admin", "Manager")
                .requestMatchers("/api/v1/master**")
                    .hasAnyAuthority("Admin", "Manager", "Master")
                .anyRequest()
                    .authenticated()
                .and()
                    .exceptionHandling()
                    .defaultAuthenticationEntryPointFor(authenticationEntryPoint(), new AntPathRequestMatcher("/api/**"))
                .and()
                    .authenticationProvider(authProvider)
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()));
        return http.build();
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }

}
