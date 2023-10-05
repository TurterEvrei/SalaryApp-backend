package com.example.salaryapp.repositories;

import com.example.salaryapp.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Long> {

}
