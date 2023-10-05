package com.example.salaryapp.dto;

import com.example.salaryapp.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private Long id;
    private String name;
    private Boolean active;
    private Long user;
    private List<Long> departments;

}
