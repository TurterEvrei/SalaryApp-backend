package com.example.salaryapp.controllers;

import com.example.salaryapp.domain.DatePeriod;
import com.example.salaryapp.domain.StatisticData;
import com.example.salaryapp.dto.DailyReportDTO;
import com.example.salaryapp.dto.EmployeeDTO;
import com.example.salaryapp.entities.DailyReport;
import com.example.salaryapp.entities.Employee;
import com.example.salaryapp.entities.enums.DatePeriodType;
import com.example.salaryapp.entities.enums.StatType;
import com.example.salaryapp.mappers.Mapper;
import com.example.salaryapp.services.DateUtils;
import com.example.salaryapp.services.dailyReport.DailyReportService;
import com.example.salaryapp.services.employee.EmployeeService;
import com.example.salaryapp.services.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/master")
@RequiredArgsConstructor
public class MasterController {

    private final EmployeeService employeeService;
    private final DailyReportService dailyReportService;
    private final PaymentService paymentService;
    private final DateUtils dateUtils;
    private final Mapper mapper;

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartmentId(@RequestParam("departmentId") Long departmentId) {
        List<Employee> employees = employeeService.getEmployeesFromDepartment(departmentId);
        return ResponseEntity.ok(employees == null ? null : employees.stream()
                .map(mapper::convertEmployeeToDto).collect(Collectors.toList()));
    }

    @GetMapping("/dailyReports")
    public ResponseEntity<List<DailyReportDTO>> getDailyReportsByDatePeriodType(
            @RequestParam Long departmentId,
            @RequestParam DatePeriodType datePeriodType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate dateStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate dateFinish
            ) {
        List<DailyReport> result = datePeriodType == DatePeriodType.CUSTOM
                ? dailyReportService.getDailyReportsByDepartment(departmentId, dateStart, dateFinish)
                : dailyReportService.getDailyReportsByDepartment(departmentId, datePeriodType);

        return ResponseEntity.ok(result.stream().map(mapper::convertDailyReportToDto).toList());
    }

    @PostMapping("/dailyReport")
    public ResponseEntity<DailyReportDTO> createDailyReport(@RequestBody DailyReportDTO dailyReportDTO) {
        DailyReport result = dailyReportService.saveDailyReport(mapper.convertDtoToDailyReport(dailyReportDTO));
        return ResponseEntity.ok(result == null
                ? null
                : mapper.convertDailyReportToDto(result));
    }

    @PutMapping("/dailyReport")
    public ResponseEntity<DailyReportDTO> editDailyReport(@RequestBody DailyReportDTO dailyReportDTO) {
        DailyReport result = dailyReportService.editDailyReport(mapper.convertDtoToDailyReport(dailyReportDTO));
        return ResponseEntity.ok(result == null
                ? null
                : mapper.convertDailyReportToDto(result));
    }

    @DeleteMapping("/dailyReport/{id}")
    public ResponseEntity<Boolean> deleteDailyReport(@PathVariable Long id) {
        return ResponseEntity.ok(dailyReportService.deleteDailyReport(id));
    }

    @GetMapping("/statistic")
    public ResponseEntity<StatisticData> getStatisticData(
            @RequestParam Long departmentId,
            @RequestParam DatePeriodType periodType,
            @RequestParam StatType statType,
            @RequestParam(required = false) LocalDate dateStart,
            @RequestParam(required = false) LocalDate dateFinish,
            Principal principal
    ) {
        StatisticData res;
        if (periodType == DatePeriodType.CUSTOM) {
            res = paymentService.getStatisticData(departmentId, principal, statType, dateStart, dateFinish);
        } else {
            DatePeriod datePeriod = dateUtils.getDatesOfPeriodType(periodType);
            res = paymentService.getStatisticData(departmentId, principal, statType, datePeriod.getDateStart(), datePeriod.getDateFinish());
        }
        return ResponseEntity.ok(res);
    }

}
