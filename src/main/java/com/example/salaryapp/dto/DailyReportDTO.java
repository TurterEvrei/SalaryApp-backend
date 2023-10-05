package com.example.salaryapp.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DailyReportDTO {

    private Long id;
    private List<PaymentDTO> payments;
    private Long department;
    private Date date;
    private Date dateOfCreated;

}
