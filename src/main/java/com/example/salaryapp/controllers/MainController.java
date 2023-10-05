package com.example.salaryapp.controllers;

import com.example.salaryapp.dto.DepartmentDTO;
import com.example.salaryapp.dto.EmployeeDTO;
import com.example.salaryapp.dto.UserDTO;
import com.example.salaryapp.entities.Department;
import com.example.salaryapp.entities.Employee;
import com.example.salaryapp.mappers.Mapper;
import com.example.salaryapp.services.department.DepartmentService;
import com.example.salaryapp.services.employee.EmployeeService;
import com.example.salaryapp.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final Mapper mapper;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(Principal principal) {
        return ResponseEntity.ok(mapper.convertUserToDto(userService.getUserByPrincipal(principal)));
    }

    @GetMapping("/employee")
    public ResponseEntity<EmployeeDTO> getUserEmployee(Principal principal) {
        Employee employee = employeeService.getEmployeeByPrincipal(principal);
        return ResponseEntity.ok(employee == null ? null : mapper.convertEmployeeToDto(employee));
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDTO>> getUserDepartments(Principal principal) {

        List<Department> departments = departmentService.getDepartmentsByPrincipal(principal);

        return ResponseEntity.ok(
                departments == null ? null
                        : departments.stream().map(mapper::convertDepartmentToDto).collect(Collectors.toList())
        );
    }

}
