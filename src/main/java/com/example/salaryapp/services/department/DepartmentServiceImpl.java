package com.example.salaryapp.services.department;

import com.example.salaryapp.entities.Department;
import com.example.salaryapp.repositories.DepartmentRepo;
import com.example.salaryapp.services.employee.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepo departmentRepo;
    private final EmployeeService employeeService;

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    @Override
    public Department getDepartment(Long id) {
        return departmentRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("No department with id: " + id));
    }

    @Override
    public List<Department> getDepartmentsByPrincipal(Principal principal) {
        try {
            return employeeService.getEmployeeByPrincipal(principal).getDepartments();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    @Override
    public Department createDepartment(Department department) {
        return null;
    }

    @Override
    public Department editDepartment(Department department) {
        return null;
    }

    @Override
    public Boolean deleteDepartment(Long id) {
        return null;
    }

    @Override
    public Boolean deleteDepartments(List<Department> departments) {
        return null;
    }
}
