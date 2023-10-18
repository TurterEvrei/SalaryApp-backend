package com.example.salaryapp.services.department;

import com.example.salaryapp.dto.DepartmentDTO;
import com.example.salaryapp.entities.Department;
import com.example.salaryapp.entities.Employee;
import com.example.salaryapp.exceptions.NoSuchEntityExeption;
import com.example.salaryapp.mappers.Mapper;
import com.example.salaryapp.repositories.DepartmentRepo;
import com.example.salaryapp.repositories.EmployeeRepo;
import com.example.salaryapp.services.EntityUtils;
import com.example.salaryapp.services.employee.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepo departmentRepo;
    private final EmployeeRepo employeeRepo;
    private final Mapper mapper;
    private final EmployeeService employeeService;

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepo.findAll().stream()
                .map(mapper::convertDepartmentToDto)
                .collect(Collectors.toList());
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
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        return mapper.convertDepartmentToDto(
                departmentRepo.save(mapper.convertDtoToDepartment(departmentDTO))
        );
    }

    @Override
    public Boolean editDepartment(DepartmentDTO departmentDTO) {
        try {
            Department department = mapper.convertDtoToDepartment(departmentDTO);
            EntityUtils.checkEntityExist(departmentRepo, department, department.getId());
            departmentRepo.save(department);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Boolean editDepartments(List<DepartmentDTO> departmentDTOList) {
        try {
            List<Department> departments = departmentDTOList.stream()
                    .map(mapper::convertDtoToDepartment).toList();
            departments.forEach(dep -> EntityUtils.checkEntityExist(departmentRepo, dep, dep.getId()));
            departmentRepo.saveAll(departments);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Boolean deleteDepartment(Long id) {
        try {
            Department storedDepartment = departmentRepo.findById(id).orElseThrow(() ->
                    new NoSuchEntityExeption(id, Department.class));
            List<Employee> employees = storedDepartment.getEmployees();
            employees.forEach(employee -> employee.getDepartments().remove(storedDepartment));
            employeeRepo.saveAll(employees);
            storedDepartment.setEmployees(Collections.emptyList());
            departmentRepo.delete(storedDepartment);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Boolean deleteDepartments(List<Department> departments) {
        return null;
    }
}
