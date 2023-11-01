package com.example.salaryapp.domain;

import com.example.salaryapp.dto.WishDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleData {

    private List<DayData> weekDates;
    private List<ScheduleRowData> rowsData;

}
