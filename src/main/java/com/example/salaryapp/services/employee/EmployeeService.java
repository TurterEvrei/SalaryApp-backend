package com.example.salaryapp.services.employee;

import com.example.salaryapp.entities.Department;
import com.example.salaryapp.entities.Employee;

import java.security.Principal;
import java.util.List;

public interface EmployeeService {

    Employee getEmployeeById(Long id);
    Employee getEmployeeByPrincipal(Principal principal);
    List<Employee> getAllEmployees();
    List<Employee> getEmployeesFromDepartment(Department department);
    List<Employee> getEmployeesFromDepartment(Long departmentId);
    List<Employee> getActiveEmployeesFromDepartment(Department department);
    Employee createEmployee(Employee employee);
    Boolean editEmployee(Employee employee);
    Boolean editEmployees(List<Employee> employees);
    Boolean deleteEmployee(Long id);
    Boolean deleteEmployees(List<Employee> employees);

}
