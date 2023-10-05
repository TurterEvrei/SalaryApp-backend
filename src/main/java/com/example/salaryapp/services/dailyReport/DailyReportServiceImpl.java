package com.example.salaryapp.services.dailyReport;

import com.example.salaryapp.dto.DailyReportDTO;
import com.example.salaryapp.entities.DailyReport;
import com.example.salaryapp.mappers.Mapper;
import com.example.salaryapp.repositories.DailyReportRepo;
import com.example.salaryapp.repositories.PaymentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyReportServiceImpl implements DailyReportService {

    private final DailyReportRepo dailyReportRepo;
    private final PaymentRepo paymentRepo;
    private final Mapper mapper;

    @Override
    public List<DailyReport> getAllDailyReports() {
        return dailyReportRepo.findAll();
    }

    @Override
    public DailyReportDTO saveDailyReport(DailyReportDTO dailyReportDTO) {

        DailyReport dailyReport = mapper.convertDtoToDailyReport(dailyReportDTO);
        dailyReport.setDateOfCreated(new Date());
        dailyReportRepo.save(dailyReport);
        dailyReport.getPayments().forEach(payment -> {
            payment.setDailyReport(dailyReport);
            paymentRepo.save(payment);
        });

        System.out.println(dailyReport.getId());
//        System.out.println(dailyReport.getDepartment().getName());
//        System.out.println(dailyReport.getDate());
//        dailyReport.getPayments().stream().map(payment -> {
//            System.out.println(payment.getEmployee().getName());
//            System.out.println(payment.getTotalPayment());
//            return null;
//        }).collect(Collectors.toList());
        return null;
//        return mapper.convertDailyReportToDto(dailyReportRepo.save(dailyReport));
    }



}
