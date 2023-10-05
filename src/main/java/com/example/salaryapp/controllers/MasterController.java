package com.example.salaryapp.controllers;

import com.example.salaryapp.dto.DailyReportDTO;
import com.example.salaryapp.dto.EmployeeDTO;
import com.example.salaryapp.entities.DailyReport;
import com.example.salaryapp.entities.Employee;
import com.example.salaryapp.mappers.Mapper;
import com.example.salaryapp.services.dailyReport.DailyReportService;
import com.example.salaryapp.services.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/master")
@RequiredArgsConstructor
public class MasterController {

    private final EmployeeService employeeService;
    private final DailyReportService dailyReportService;
    private final Mapper mapper;

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartmentId(@RequestParam("departmentId") Long departmentId) {
        List<Employee> employees = employeeService.getEmployeesFromDepartment(departmentId);
        return ResponseEntity.ok(employees == null ? null : employees.stream()
                .map(mapper::convertEmployeeToDto).collect(Collectors.toList()));
    }

    @PostMapping("/dailyReport")
    public ResponseEntity<DailyReportDTO> saveDailyReport(@RequestBody DailyReportDTO dailyReportDTO) {
        return ResponseEntity.ok(dailyReportService.saveDailyReport(dailyReportDTO));
    }

}
