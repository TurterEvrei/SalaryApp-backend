package com.example.salaryapp.repositories;

import com.example.salaryapp.entities.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WishRepo extends JpaRepository<Wish, Long> {

    Optional<Wish> findWishByEmployee_IdAndDateTimeBetween(Long employeeId, LocalDateTime start, LocalDateTime finish);
    List<Wish> findWishesByEmployee_IdAndDepartment_IdAndDateTimeBetween(Long employeeId, Long departmentId, LocalDateTime start, LocalDateTime finish);

}
