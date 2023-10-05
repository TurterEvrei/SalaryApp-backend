package com.example.salaryapp.controllers;

import com.example.salaryapp.dto.*;
import com.example.salaryapp.entities.*;
import com.example.salaryapp.mappers.Mapper;
import com.example.salaryapp.repositories.EmployeeRepo;
import com.example.salaryapp.repositories.UserRepo;
import com.example.salaryapp.services.dailyReport.DailyReportService;
import com.example.salaryapp.services.department.DepartmentService;
import com.example.salaryapp.services.employee.EmployeeService;
import com.example.salaryapp.services.payment.PaymentService;
import com.example.salaryapp.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PaymentService paymentService;
    private final DailyReportService dailyReportService;
    private final EmployeeRepo employeeRepo;
    private final UserRepo userRepo;
    private final Mapper mapper;

    @PostMapping
    public ResponseEntity<DailyReportDTO> testPost(@RequestBody DailyReportDTO dailyReportDTO) {

        DailyReport dailyReport = mapper.convertDtoToDailyReport(dailyReportDTO);
        System.out.println(dailyReport.getDepartment().getName());
        System.out.println(dailyReport.getPayments().stream()
                .map(rep -> rep.getEmployee().getName()).collect(Collectors.toList()));
        System.out.println(dailyReport.getId());

        return ResponseEntity.ok(mapper.convertDailyReportToDto(dailyReport));
    }

    @GetMapping
    public ResponseEntity<List<DailyReportDTO>> testGet() {
        return ResponseEntity.ok(dailyReportService.getAllDailyReports().stream()
                .map(mapper::convertDailyReportToDto)
                .collect(Collectors.toList()));
    }

}
