package com.example.salaryapp.repositories;

import com.example.salaryapp.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepo extends JpaRepository<Department, Long> {

}
