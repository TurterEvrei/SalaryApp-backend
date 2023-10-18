package com.example.salaryapp.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class PaymentDTO {

    private Long id;
    private Integer procentFromSales;
    private Integer tips;
    private Integer totalPayment;
    private Long employeeId;
    private String employeeName;
    private LocalDate date;

}
