package com.example.salaryapp.repositories;

import com.example.salaryapp.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepo extends JpaRepository<Payment, Long> {

    @Query("select distinct payment from Payment payment join payment.dailyReport report " +
            "where payment.employee.id =:employeeId " +
            "and report.department.id =:departmentId " +
            "and report.date between :dateStart and :dateFinish order by report.date")
    List<Payment> findByEmployeeIdAndDepartmentsIdBetweenDates(Long employeeId, Long departmentId, LocalDate dateStart, LocalDate dateFinish);

}
