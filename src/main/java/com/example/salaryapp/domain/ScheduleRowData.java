package com.example.salaryapp.domain;

import com.example.salaryapp.dto.WishDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScheduleRowData {

    private Long employeeId;
    private String employeeName;
    private List<WishDTO> wishes;

}
