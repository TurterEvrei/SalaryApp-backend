package com.example.salaryapp.services.payment;

import com.example.salaryapp.domain.StatisticData;
import com.example.salaryapp.entities.Payment;
import com.example.salaryapp.entities.enums.StatType;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public interface PaymentService {

    List<Payment> getAllPayments();
    StatisticData getStatisticData(Long departmentId, Principal principal, StatType statType, LocalDate dateStart, LocalDate dateFinish);

}
