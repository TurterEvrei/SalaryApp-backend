package com.example.salaryapp.controllers;

import com.example.salaryapp.dto.EmployeeDTO;
import com.example.salaryapp.dto.UserDTO;
import com.example.salaryapp.entities.User;
import com.example.salaryapp.mappers.Mapper;
import com.example.salaryapp.services.employee.EmployeeService;
import com.example.salaryapp.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NameAlreadyBoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final Mapper mapper;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(
                userService
                        .getAllUsers().stream()
                        .map(mapper::convertUserToDto)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) throws NameAlreadyBoundException {
        return ResponseEntity.ok(mapper.convertUserToDto(
                userService.createUser(mapper.convertDtoToUser(userDTO))
        ));
    }

    @PutMapping("/users")
    public ResponseEntity<UserDTO> editUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(mapper.convertUserToDto(
                userService.editUser(mapper.convertDtoToUser(userDTO))
        ));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @DeleteMapping("/users")
    public ResponseEntity<Boolean> deleteUsers(@RequestBody List<User> users) {
        return ResponseEntity.ok(userService.deleteUsers(users));
    }

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(
                employeeService
                        .getAllEmployees().stream()
                        .map(mapper::convertEmployeeToDto)
                        .collect(Collectors.toList())
        );
    }

}
