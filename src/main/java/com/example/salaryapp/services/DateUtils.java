package com.example.salaryapp.services;

import com.example.salaryapp.dto.domain.DatePeriod;
import com.example.salaryapp.entities.enums.DatePeriodType;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static DatePeriod getDatesOfPeriodType(DatePeriodType datePeriodType) {
        Calendar start = Calendar.getInstance();
        start.setTime(new Date());
        Calendar finish = Calendar.getInstance();
        finish.setTime(new Date());

        if (start.get(Calendar.DAY_OF_MONTH) > 15) {
            if (datePeriodType == DatePeriodType.LAST) {
                takeMonthFirstPart(start, finish);
            }
            if (datePeriodType == DatePeriodType.PRESENT) {
                takeMonthSecondPart(start,finish);
            }
        } else {
            if (datePeriodType == DatePeriodType.LAST) {
                start.add(Calendar.MONTH, -1);
                finish.add(Calendar.MONTH, -1);
                takeMonthSecondPart(start,finish);
            }
            if (datePeriodType == DatePeriodType.PRESENT) {
                takeMonthFirstPart(start, finish);
            }
        }

        return DatePeriod.builder()
                .dateStart(LocalDate.ofInstant(start.toInstant(), ZoneId.systemDefault()))
                .dateFinish(LocalDate.ofInstant(finish.toInstant(), ZoneId.systemDefault()))
                .build();
    }

    private static void takeMonthFirstPart(Calendar start, Calendar finish) {
        start.set(Calendar.DAY_OF_MONTH, 1);
        finish.set(Calendar.DAY_OF_MONTH, 15);
    }

    private static void takeMonthSecondPart(Calendar start, Calendar finish) {
        start.set(Calendar.DAY_OF_MONTH, 16);
        finish.set(Calendar.DAY_OF_MONTH, finish.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

}
