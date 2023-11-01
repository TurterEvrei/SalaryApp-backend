package com.example.salaryapp.dto;

import com.example.salaryapp.entities.enums.WishType;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class WishDTO {

    private Long id;
    private WishType type;
    private LocalDateTime dateTime;
    private LocalTime endTime;
    private Long employee;
    private Long department;
    private Boolean fixed;

}
