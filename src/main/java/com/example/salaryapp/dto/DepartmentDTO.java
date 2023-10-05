package com.example.salaryapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentDTO {

    private Long id;
    private String name;
    private List<Long> employees;
    private Float calcSetting;

}
