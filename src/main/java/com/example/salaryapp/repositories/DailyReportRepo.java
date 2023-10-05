package com.example.salaryapp.repositories;

import com.example.salaryapp.entities.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyReportRepo extends JpaRepository<DailyReport, Long> {

}
