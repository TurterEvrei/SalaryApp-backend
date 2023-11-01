package com.example.salaryapp.services.payment;

import com.example.salaryapp.domain.StatisticData;
import com.example.salaryapp.entities.DailyReport;
import com.example.salaryapp.entities.Employee;
import com.example.salaryapp.entities.Payment;
import com.example.salaryapp.entities.User;
import com.example.salaryapp.entities.enums.StatType;
import com.example.salaryapp.repositories.DailyReportRepo;
import com.example.salaryapp.repositories.EmployeeRepo;
import com.example.salaryapp.repositories.PaymentRepo;
import com.example.salaryapp.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final EmployeeRepo employeeRepo;
    private final UserRepo userRepo;
    private final DailyReportRepo dailyReportRepo;
    private final String DATE_KEY = "date";
    private final String DATE_FORMATTER_PATTERN = "dd";
    private final Integer DAYS_LIMIT = 32;

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }

    @Override
    public StatisticData getStatisticData(Long departmentId, Principal principal, StatType statType, LocalDate dateStart, LocalDate dateFinish) {
        try {
            int total = 0;
            Set<String> keys = new HashSet<>();
            LinkedList<Map<String, String>> chartData = new LinkedList<>();
            LinkedHashMap<String, Integer> tableData = new LinkedHashMap<>();

            if (statType == StatType.STAT_OWN) {
                User currentUser = userRepo.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("No user with email: " + principal.getName()));
                Employee currentEmployee = currentUser.getEmployee();
                List<Payment> payments = paymentRepo.findByEmployeeIdAndDepartmentsIdBetweenDates(
                        currentEmployee.getId(), departmentId, dateStart, dateFinish
                    );
                if (payments.size() < DAYS_LIMIT) {
                    for (Payment payment : payments) {
                        Map<String, String> dataElement = new LinkedHashMap<>();
                        dataElement.put(DATE_KEY, payment.getDailyReport().getDate().format(DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN)));
                        dataElement.put(payment.getEmployee().getName(), payment.getTotalPayment().toString());
                        chartData.add(dataElement);
                        tableData.put(payment.getDailyReport().getDate().toString(), payment.getTotalPayment());
                    };
                } else {
                    payments.forEach(payment -> {
                        Month month = payment.getDailyReport().getDate().getMonth();
                        Map<String, String> dataElement = chartData.stream()
                                .filter(el -> el.get(DATE_KEY).equals(month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)))
                                .findFirst()
                                .orElse(new LinkedHashMap<>());
                        dataElement.merge(
                                payment.getEmployee().getName(),
                                payment.getTotalPayment().toString(),
                                (v1, v2) -> String.valueOf(Integer.parseInt(v1) + Integer.parseInt(v2)));
                        if (dataElement.get(DATE_KEY) == null) {
                            dataElement.put(DATE_KEY, month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
                            chartData.add(dataElement);
                        }
                        tableData.put(payment.getDailyReport().getDate().toString(), payment.getTotalPayment());
                    });
                }

                total = tableData.values().stream().reduce(0, Integer::sum);
                keys = Set.of(currentEmployee.getName());
            } else {
                List<DailyReport> dailyReports = dailyReportRepo
                        .findDailyReportsByDateBetweenAndDepartment_IdOrderByDate(dateStart, dateFinish, departmentId);

                if (dailyReports.size() < DAYS_LIMIT) {
                    for (DailyReport report : dailyReports) {
                        Map<String, String> dataElement = new LinkedHashMap<>();
                        dataElement.put(DATE_KEY, report.getDate().format(DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN)));
                        report.getPayments().forEach(payment -> {
                            dataElement.put(payment.getEmployee().getName(), payment.getTotalPayment().toString());
                            tableData.merge(payment.getEmployee().getName(), payment.getTotalPayment(), Integer::sum);
                        });
                        chartData.add(dataElement);
                    }
                } else {
                    dailyReports.forEach(report -> {
                        Month month = report.getDate().getMonth();
                        Map<String, String> dataElement = chartData.stream()
                                .filter(el -> el.get(DATE_KEY).equals(month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)))
                                .findFirst()
                                .orElse(new LinkedHashMap<>());
                        report.getPayments().forEach(payment -> {
                            dataElement.merge(
                                    payment.getEmployee().getName(),
                                    payment.getTotalPayment().toString(),
                                    (v1, v2) -> String.valueOf(Integer.parseInt(v1) + Integer.parseInt(v2)));
                            tableData.merge(payment.getEmployee().getName(), payment.getTotalPayment(), Integer::sum);
                        });
                        if (dataElement.get(DATE_KEY) == null) {
                            dataElement.put(DATE_KEY, month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
                            chartData.add(dataElement);
                        }
                    });
                }
                total = tableData.values().stream().reduce(0, Integer::sum);
                keys = employeeRepo.findActiveEmployeesByDepartmentId(departmentId)
                        .stream()
                        .map(Employee::getName)
                        .collect(Collectors.toSet());
            }
            return new StatisticData(total, keys, chartData,tableData);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
