package com.example.salaryapp.services;

import com.example.salaryapp.domain.DatePeriod;
import com.example.salaryapp.domain.DayData;
import com.example.salaryapp.domain.Pagination;
import com.example.salaryapp.entities.enums.DatePeriodType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class DateUtils {

    private final static String[] daysOfWeek = new String[]{"Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};

    public List<DayData> getDayDataList(LocalDate date, Pagination pagination, int curIndex) {

        Calendar datePoint = Calendar.getInstance();
        datePoint.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        System.out.println(datePoint.get(Calendar.DATE));
        datePoint.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        datePoint.add(Calendar.WEEK_OF_YEAR, pagination.getPageSize() * pagination.getPageIndex() + curIndex);
        System.out.println(datePoint.get(Calendar.DATE));

        List<DayData> dates = new LinkedList<>();
        for (int i = 0; i < 7; i++) {
            dates.add(new DayData(LocalDate.ofInstant(datePoint.toInstant(), ZoneId.systemDefault()), daysOfWeek[i]));
            datePoint.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public DatePeriod getDatesOfPeriodType(DatePeriodType datePeriodType) {
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
