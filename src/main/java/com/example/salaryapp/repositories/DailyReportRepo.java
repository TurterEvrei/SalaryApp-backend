package com.example.salaryapp.repositories;

import com.example.salaryapp.entities.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DailyReportRepo extends JpaRepository<DailyReport, Long> {
    List<DailyReport> findDailyReportsByDateBetweenAndDepartment_Id(LocalDate start, LocalDate Finish, Long departmentId);
    Optional<DailyReport> findDailyReportByDateAndDepartment_Id(LocalDate date, Long departmentId);
}
