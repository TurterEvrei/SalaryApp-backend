package com.example.salaryapp.mappers;

import com.example.salaryapp.dto.*;
import com.example.salaryapp.entities.*;
import com.example.salaryapp.repositories.DepartmentRepo;
import com.example.salaryapp.repositories.EmployeeRepo;
import com.example.salaryapp.repositories.PaymentRepo;
import com.example.salaryapp.repositories.UserRepo;
import com.example.salaryapp.services.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Mapper {

    private final ModelMapper modelMapper;
    private final EmployeeService employeeService;
    private final UserRepo userRepo;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    private final PaymentRepo paymentRepo;

    public UserDTO convertUserToDto(User user) {
        return modelMapper.typeMap(User.class, UserDTO.class)
                .addMappings(mapper -> mapper.when(
                        ctx -> user.getEmployee() != null)
                        .map(
                                src -> src.getEmployee().getId(),
                                UserDTO::setEmployee))
                .addMappings(mapper -> mapper.skip(UserDTO::setPassword))
                .map(user);
    }

    public User convertDtoToUser(UserDTO userDTO) {
        String password = userDTO.getPassword();
        if (userDTO.getId() != null) {
            User storedUser = userRepo.findById(userDTO.getId()).orElse(null);
            if (storedUser != null && (password == null || password.isEmpty())) {
                userDTO.setPassword(storedUser.getPassword());
            }
        }

        User user = modelMapper.typeMap(UserDTO.class, User.class)
                .addMappings(mapper -> mapper.skip(UserDTO::getEmployee, User::setEmployee))
                .map(userDTO);

        if (userDTO.getEmployee() != null) {
            user.setEmployee(employeeService.getEmployeeById(userDTO.getEmployee()));
        }
        return user;
    }

    public EmployeeDTO convertEmployeeToDto(Employee employee) {
        return modelMapper.typeMap(Employee.class, EmployeeDTO.class)
                .addMapping(
                        emp -> emp.getUser().getId(),
                        EmployeeDTO::setUser)
                .addMappings(mapper -> mapper.using(
                        (Converter<List<Department>, List<Long>>) ctx ->
                                ctx.getSource().stream()
                                        .map(Department::getId)
                                        .collect(Collectors.toList()))
                        .map(Employee::getDepartments, EmployeeDTO::setDepartments))
                .map(employee);
    }

    public Employee convertDtoToEmployee(EmployeeDTO employeeDTO) {
        return modelMapper.typeMap(EmployeeDTO.class, Employee.class)
                .addMappings(mapper -> mapper.using(
                        (Converter<List<Long>, List<Department>>) ctx ->
                                departmentRepo.findAllById(ctx.getSource()))
                        .map(EmployeeDTO::getDepartments, Employee::setDepartments))
                .addMappings(mapper -> mapper.using(
                        (Converter<Long, User>) ctx ->
                                ctx.getSource() != null
                                        ? userRepo.findById(ctx.getSource()).orElse(null)
                                        : null)
                        .map(EmployeeDTO::getUser, Employee::setUser))
                .map(employeeDTO);
    }

    public DepartmentDTO convertDepartmentToDto(Department department) {
        return modelMapper.typeMap(Department.class, DepartmentDTO.class)
                .addMappings(mapper -> mapper.using(
                        (Converter<List<Employee>, List<Long>>) ctx ->
                                ctx.getSource().stream()
                                        .map(Employee::getId)
                                        .collect(Collectors.toList()))
                        .map(Department::getEmployees, DepartmentDTO::setEmployees))
                .map(department);
    }

    public Department convertDtoToDepartment(DepartmentDTO departmentDTO) {
        return modelMapper.typeMap(DepartmentDTO.class, Department.class)
                .addMappings(mapper -> mapper.using(
                        (Converter<List<Long>, List<Employee>>) ctx ->
                                employeeRepo.findAllById(ctx.getSource()))
                        .map(DepartmentDTO::getEmployees, Department::setEmployees))
                .map(departmentDTO);
    }

    public PaymentDTO convertPaymentToDto(Payment payment) {
        return modelMapper.typeMap(Payment.class, PaymentDTO.class)
                .addMappings(mapper -> mapper.using(
                        (Converter<Employee, Long>) ctx ->
                                ctx.getSource().getId())
                        .map(Payment::getEmployee, PaymentDTO::setEmployeeId))
                .addMappings(mapper -> mapper.using(
                        (Converter<Employee, String>) ctx ->
                                ctx.getSource().getName())
                        .map(Payment::getEmployee, PaymentDTO::setEmployeeName))
                .addMappings(mapper -> mapper.using(
                        (Converter<DailyReport, LocalDate>) ctx ->
                                ctx.getSource().getDate())
                        .map(Payment::getDailyReport, PaymentDTO::setDate))
                .map(payment);
    }

    public Payment convertDtoToPayment(PaymentDTO paymentDTO) {
        return modelMapper.typeMap(PaymentDTO.class, Payment.class)
                .addMappings(mapper -> mapper.using(
                        (Converter<Long, Employee>) ctx ->
                                employeeRepo.findById(ctx.getSource()).orElse(null))
                        .map(PaymentDTO::getEmployeeId, Payment::setEmployee))
                .addMappings(mapper -> mapper.using(
                        (Converter<Long, DailyReport>) ctx ->
                                ctx.getSource() != null
                                        ? Objects.requireNonNull(
                                                paymentRepo.findById(ctx.getSource()).orElse(null))
                                        .getDailyReport()
                                        : null)
                        .map(PaymentDTO::getId, Payment::setDailyReport))
                .map(paymentDTO);
    }

    public DailyReportDTO convertDailyReportToDto(DailyReport dailyReport) {
        return modelMapper.typeMap(DailyReport.class, DailyReportDTO.class)
                .addMappings(mapper -> mapper.using(
                        (Converter<List<Payment>, List<PaymentDTO>>) ctx ->
                                ctx.getSource().stream()
                                        .map(this::convertPaymentToDto)
                                        .collect(Collectors.toList()))
                        .map(DailyReport::getPayments, DailyReportDTO::setPayments))
                .addMappings(mapper -> mapper.using(
                        (Converter<Department, Long>) ctx ->
                                ctx.getSource().getId())
                        .map(DailyReport::getDepartment, DailyReportDTO::setDepartment))
                .map(dailyReport);
    }

    public DailyReport convertDtoToDailyReport(DailyReportDTO dailyReportDTO) {
        return modelMapper.typeMap(DailyReportDTO.class, DailyReport.class)
                .addMappings(mapper -> mapper.using(
                        (Converter<List<PaymentDTO>, List<Payment>>) ctx ->
                                ctx.getSource().stream()
                                        .map(this::convertDtoToPayment)
                                        .collect(Collectors.toList()))
                        .map(DailyReportDTO::getPayments, DailyReport::setPayments))
                .addMappings(mapper -> mapper.using(
                        (Converter<Long, Department>) ctx ->
                                departmentRepo.findById(ctx.getSource()).orElse(null))
                        .map(DailyReportDTO::getDepartment, DailyReport::setDepartment))
                .map(dailyReportDTO);
    }

    public WishDTO convertWishToDto(Wish wish) {
        return modelMapper.typeMap(Wish.class, WishDTO.class)
                .addMapping(w -> w.getEmployee().getId(), WishDTO::setEmployee)
                .addMapping(w -> w.getDepartment().getId(), WishDTO::setDepartment)
                .map(wish);
    }

    public Wish convertDtoToWish(WishDTO wishDTO) {
        return modelMapper.typeMap(WishDTO.class, Wish.class)
                .addMappings(mapper -> mapper.using(
                        (Converter<Long, Employee>) ctx ->
                                employeeRepo.findById(ctx.getSource()).orElse(null))
                        .map(WishDTO::getEmployee, Wish::setEmployee))
                .addMappings(mapper -> mapper.using(
                        (Converter<Long, Department>) ctx ->
                                departmentRepo.findById(ctx.getSource()).orElse(null))
                        .map(WishDTO::getDepartment, Wish::setDepartment))
                .map(wishDTO);
    }

}
