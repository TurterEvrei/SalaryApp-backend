package com.example.salaryapp.services.department;

import com.example.salaryapp.dto.DepartmentDTO;
import com.example.salaryapp.entities.Department;

import java.security.Principal;
import java.util.List;

public interface DepartmentService {

    List<DepartmentDTO> getAllDepartments();
    Department getDepartment(Long id);
    List<Department> getDepartmentsByPrincipal(Principal principal);
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
    Boolean editDepartment(DepartmentDTO departmentDTO);
    Boolean editDepartments(List<DepartmentDTO> departmentDTOList);
    Boolean deleteDepartment(Long id);
    Boolean deleteDepartments(List<Department> departments);

}
