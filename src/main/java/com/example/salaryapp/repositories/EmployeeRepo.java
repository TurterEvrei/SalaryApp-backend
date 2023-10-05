package com.example.salaryapp.repositories;

import com.example.salaryapp.entities.Department;
import com.example.salaryapp.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    @Query("select distinct emp from Employee emp join emp.departments dep where dep =:department")
    List<Employee> findByDepartment(Department department);

    @Query("select distinct emp from Employee emp join emp.departments dep where dep =:department and emp.active = true")
    List<Employee> findActiveByDepartment(Department department);

}
