package com.example.salaryapp.services.employee;

import com.example.salaryapp.entities.Department;
import com.example.salaryapp.entities.Employee;
import com.example.salaryapp.repositories.DepartmentRepo;
import com.example.salaryapp.repositories.EmployeeRepo;
import com.example.salaryapp.services.EntityUtils;
import com.example.salaryapp.services.department.DepartmentService;
import com.example.salaryapp.services.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    private final UserService userService;

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepo.findById(id).orElseThrow(() ->
                new NoSuchElementException("No employee with id: " + id));
    }

    @Override
    public Employee getEmployeeByPrincipal(Principal principal) {
        return userService.getUserByPrincipal(principal).getEmployee();
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public List<Employee> getEmployeesFromDepartment(Department department) {
        return employeeRepo.findByDepartment(department);
    }

    @Override
    public List<Employee> getEmployeesFromDepartment(Long departmentId) {
        try {
            return departmentRepo.findById(departmentId)
                    .orElseThrow(() -> new EntityNotFoundException("No department with id: " + departmentId))
                    .getEmployees();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public List<Employee> getActiveEmployeesFromDepartment(Department department) {
        return employeeRepo.findActiveByDepartment(department);
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepo.save(employee);
    }

    @Override
    public Employee editEmployee(Employee employee) {
        return EntityUtils.editEntity(employeeRepo, employee, employee.getId());
    }

    @Override
    public Boolean deleteEmployee(Long id) {
        return EntityUtils.deleteEntity(employeeRepo, id);
    }

    @Override
    public Boolean deleteEmployees(List<Employee> employees) {
        return EntityUtils.deleteEntities(employeeRepo, employees);
    }
}
