package com.example.salaryapp.services.dailyReport;

import com.example.salaryapp.domain.DatePeriod;
import com.example.salaryapp.entities.DailyReport;
import com.example.salaryapp.entities.Payment;
import com.example.salaryapp.entities.enums.DatePeriodType;
import com.example.salaryapp.exceptions.WrongIdForEditException;
import com.example.salaryapp.mappers.Mapper;
import com.example.salaryapp.repositories.DailyReportRepo;
import com.example.salaryapp.repositories.EmployeeRepo;
import com.example.salaryapp.repositories.PaymentRepo;
import com.example.salaryapp.services.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyReportServiceImpl implements DailyReportService {

    private final DailyReportRepo dailyReportRepo;
    private final PaymentRepo paymentRepo;
    private final DateUtils dateUtils;

    @Override
    public List<DailyReport> getAllDailyReports() {
        return dailyReportRepo.findAll();
    }

    @Override
    public List<DailyReport> getDailyReportsByDepartment(Long departmentId, DatePeriodType datePeriodType) {
        DatePeriod datePeriod = dateUtils.getDatesOfPeriodType(datePeriodType);
        return dailyReportRepo.findDailyReportsByDateBetweenAndDepartment_IdOrderByDate(
                datePeriod.getDateStart(),
                datePeriod.getDateFinish(),
                departmentId
        );
    }

    @Override
    public List<DailyReport> getDailyReportsByDepartment(Long departmentId, LocalDate dateStart, LocalDate dateFinish) {
        DatePeriod datePeriod = DatePeriod.builder()
                .dateStart(dateStart == null
                        ? LocalDate.now().minusDays(30)
                        : dateStart)
                .dateFinish(dateFinish == null
                        ? LocalDate.now()
                        : dateFinish)
                .build();
        return dailyReportRepo.findDailyReportsByDateBetweenAndDepartment_IdOrderByDate(
                datePeriod.getDateStart(),
                datePeriod.getDateFinish(),
                departmentId
        );
    }

    @Override
    public DailyReport saveDailyReport(DailyReport dailyReport) {
        try {
            if (dailyReportRepo
                    .findDailyReportByDateAndDepartment_Id(dailyReport.getDate(), dailyReport.getDepartment().getId())
                    .isPresent()) {
                throw new RuntimeException("Daily report already exist");
            }
            dailyReport.setDateOfCreated(new Date());
            System.out.println(dailyReport.getDate());
            dailyReportRepo.save(dailyReport);
            dailyReport.getPayments().forEach(payment -> {
                payment.setDailyReport(dailyReport);
                paymentRepo.save(payment);
            });
            System.out.println(dailyReport.getDate());
            return dailyReport;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public DailyReport editDailyReport(DailyReport dailyReport) {
        try {
            DailyReport sameDateDailyReport = dailyReportRepo
                    .findDailyReportByDateAndDepartment_Id(dailyReport.getDate(), dailyReport.getDepartment().getId())
                    .orElse(null);
            if (sameDateDailyReport != null && sameDateDailyReport.getId() != dailyReport.getId()) {
                throw new RuntimeException("This date already bounded");
            }
            DailyReport storedDailyReport = dailyReportRepo.findById(dailyReport.getId()).orElseThrow(() ->
                    new WrongIdForEditException(dailyReport.getId(), DailyReport.class));

            List<Payment> storedPayments = storedDailyReport.getPayments();

            List<Payment> paymentsToDelete = storedPayments.stream()
                    .filter(payment -> !dailyReport.getPayments().contains(payment))
                    .peek(payment -> {
                        payment.getEmployee().getPayments().remove(payment);
                    })
                    .collect(Collectors.toList());

            storedPayments.removeAll(paymentsToDelete);

            paymentRepo.deleteAll(paymentsToDelete);

            dailyReport.getPayments().forEach(payment -> {
                payment.setDailyReport(dailyReport);
            });
            return dailyReportRepo.save(dailyReport);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean deleteDailyReport(Long id) {
        try {
            dailyReportRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

}
