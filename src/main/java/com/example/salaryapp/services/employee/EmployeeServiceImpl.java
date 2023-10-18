package com.example.salaryapp.services.employee;

import com.example.salaryapp.entities.Department;
import com.example.salaryapp.entities.Employee;
import com.example.salaryapp.entities.User;
import com.example.salaryapp.exceptions.NoSuchEntityExeption;
import com.example.salaryapp.exceptions.WrongIdForEditException;
import com.example.salaryapp.repositories.DepartmentRepo;
import com.example.salaryapp.repositories.EmployeeRepo;
import com.example.salaryapp.repositories.UserRepo;
import com.example.salaryapp.services.EntityUtils;
import com.example.salaryapp.services.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    private final UserRepo userRepo;
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
        try {
            User user = employee.getUser();
            if (user != null) {
                user.setEmployee(employee);
            }
            return employeeRepo.save(employee);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public Boolean editEmployee(Employee employee) {
        try {
            preEditEmployee(employee);
            employeeRepo.save(employee);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Boolean editEmployees(List<Employee> employees) {
        try {
            employees.forEach(employee -> {
                preEditEmployee(employee);
                System.out.println(employee);
                employeeRepo.save(employee);
            });
//            employeeRepo.saveAll(employees);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean deleteEmployee(Long id) {
        try {
            Employee storedEmployee = employeeRepo.findById(id).orElseThrow(() ->
                    new NoSuchEntityExeption(id, Employee.class));
            User user = storedEmployee.getUser();
            if (user != null) {
                user.setEmployee(null);
                storedEmployee.setUser(null);
                userRepo.save(user);
            }
            List<Department> departments = storedEmployee.getDepartments();
            departments.forEach(department -> department.getEmployees().remove(storedEmployee));
            storedEmployee.setDepartments(Collections.emptyList());
            departmentRepo.saveAll(departments);

            employeeRepo.delete(storedEmployee);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Boolean deleteEmployees(List<Employee> employees) {
        return EntityUtils.deleteEntities(employeeRepo, employees);
    }

    private void preEditEmployee(Employee employee) {
        Employee storedEmployee = employeeRepo.findById(employee.getId()).orElseThrow(() ->
                new WrongIdForEditException(employee.getId(), employee));
        User oldUser = storedEmployee.getUser();
        User newUser = employee.getUser();

//        System.out.println(employee.getName());
//        System.out.println(employee.getUser().getName());
//        System.out.println(employee.getActive());

        if (oldUser != null && !oldUser.equals(newUser)) {
            oldUser.setEmployee(null);
            userRepo.save(oldUser);
        }

        if (newUser != null && !newUser.equals(oldUser)) {
//            Employee oldEmployee = newUser.getEmployee();
//            if (oldEmployee != null) {
//                oldEmployee.setUser(null);
//            }
            newUser.setEmployee(employee);
        }
    }
}
