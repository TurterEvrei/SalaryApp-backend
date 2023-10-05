package com.example.salaryapp.dto;

import com.example.salaryapp.entities.enums.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private Boolean active;
    private Set<Role> roles = new HashSet<>();
    private Long employee;

}
