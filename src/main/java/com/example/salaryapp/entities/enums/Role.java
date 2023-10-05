package com.example.salaryapp.entities.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    ADMIN("Admin"),
    MANAGER("Manager"),
    MASTER("Master"),
    USER("User");

    private final String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
