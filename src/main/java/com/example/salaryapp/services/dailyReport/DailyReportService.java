package com.example.salaryapp.services.dailyReport;

import com.example.salaryapp.entities.DailyReport;
import com.example.salaryapp.entities.enums.DatePeriodType;

import java.time.LocalDate;
import java.util.List;

public interface DailyReportService {

    List<DailyReport> getAllDailyReports();
    List<DailyReport> getDailyReportsByDepartment(Long departmentId, DatePeriodType datePeriodType);
    List<DailyReport> getDailyReportsByDepartment(Long departmentId, LocalDate dateStart, LocalDate dateFinish);
    DailyReport saveDailyReport(DailyReport dailyReport);
    DailyReport editDailyReport(DailyReport dailyReport);
    Boolean deleteDailyReport(Long id);

}
