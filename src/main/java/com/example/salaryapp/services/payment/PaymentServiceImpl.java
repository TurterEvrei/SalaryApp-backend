package com.example.salaryapp.services.payment;

import com.example.salaryapp.entities.Payment;
import com.example.salaryapp.repositories.PaymentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }
}
