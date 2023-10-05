package com.example.salaryapp.services.dailyReport;

import com.example.salaryapp.dto.DailyReportDTO;
import com.example.salaryapp.entities.DailyReport;

import java.util.List;

public interface DailyReportService {

    List<DailyReport> getAllDailyReports();
    DailyReportDTO saveDailyReport(DailyReportDTO dailyReportDTO);

}
