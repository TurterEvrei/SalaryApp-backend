package com.example.salaryapp.services.department;

import com.example.salaryapp.entities.Department;

import java.security.Principal;
import java.util.List;

public interface DepartmentService {

    List<Department> getAllDepartments();
    Department getDepartment(Long id);
    List<Department> getDepartmentsByPrincipal(Principal principal);
    Department createDepartment(Department department);
    Department editDepartment(Department department);
    Boolean deleteDepartment(Long id);
    Boolean deleteDepartments(List<Department> departments);

}
