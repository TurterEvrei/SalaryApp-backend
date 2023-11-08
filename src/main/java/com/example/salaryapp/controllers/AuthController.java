package com.example.salaryapp.controllers;

import com.example.salaryapp.domain.auth.AuthRequest;
import com.example.salaryapp.domain.auth.AuthResponse;
import com.example.salaryapp.domain.auth.RegisterRequest;
import com.example.salaryapp.services.auth.AuthenticationService;
import com.example.salaryapp.services.dailyReport.DailyReportService;
import com.example.salaryapp.services.exporter.ExporterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;

    @PostMapping("registration")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registration(request));
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        System.out.println(request);
        return ResponseEntity.ok(authService.authentication(request));
    }

    @PostMapping("refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }
}
