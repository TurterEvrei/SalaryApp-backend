package com.example.salaryapp.controllers;

import com.example.salaryapp.dto.EmployeeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manager")
public class ManagerController {

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getEmployeesFromDepartment() {
        return ResponseEntity.ok(null);
    }


}
