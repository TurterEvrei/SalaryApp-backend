package com.example.salaryapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DayData {

    private LocalDate date;
    private String day;

}
