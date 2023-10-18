package com.example.salaryapp.controllers;

import com.example.salaryapp.dto.DailyReportDTO;
import com.example.salaryapp.dto.EmployeeDTO;
import com.example.salaryapp.entities.Employee;
import com.example.salaryapp.entities.enums.DatePeriodType;
import com.example.salaryapp.mappers.Mapper;
import com.example.salaryapp.services.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final EmployeeService employeeService;
    private final Mapper mapper;

//    @GetMapping
//    public ResponseEntity<List<EmployeeDTO>> getEmployeesFromDepartment() {
//        return ResponseEntity.ok(null);
//    }

    @PostMapping("/employee")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(mapper.convertEmployeeToDto(
                employeeService.createEmployee(mapper.convertDtoToEmployee(employeeDTO))
        ));
    }

    @PutMapping("/employee")
    public ResponseEntity<Boolean> editEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.editEmployee(mapper.convertDtoToEmployee(employeeDTO)));
    }

    @PutMapping("/employees")
    public ResponseEntity<Boolean> editEmployees(@RequestBody List<EmployeeDTO> employeeDTOList) {
        return ResponseEntity.ok(employeeService.editEmployees(
                    employeeDTOList.stream().map(mapper::convertDtoToEmployee).toList()
                ));
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }

}
