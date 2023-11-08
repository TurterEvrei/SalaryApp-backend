package com.example.salaryapp.services.exporter;

import com.example.salaryapp.entities.DailyReport;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface ExporterService {

    void exportDailyReports(HttpServletResponse response, List<DailyReport> reports);

}
