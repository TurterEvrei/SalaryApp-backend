package com.example.salaryapp.controllers;

import com.example.salaryapp.domain.*;
import com.example.salaryapp.dto.DepartmentDTO;
import com.example.salaryapp.dto.EmployeeDTO;
import com.example.salaryapp.dto.UserDTO;
import com.example.salaryapp.dto.WishDTO;
import com.example.salaryapp.entities.Department;
import com.example.salaryapp.entities.Employee;
import com.example.salaryapp.entities.Wish;
import com.example.salaryapp.entities.enums.DatePeriodType;
import com.example.salaryapp.entities.enums.StatType;
import com.example.salaryapp.mappers.Mapper;
import com.example.salaryapp.services.DateUtils;
import com.example.salaryapp.services.department.DepartmentService;
import com.example.salaryapp.services.employee.EmployeeService;
import com.example.salaryapp.services.payment.PaymentService;
import com.example.salaryapp.services.user.UserService;
import com.example.salaryapp.services.wish.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PaymentService paymentService;
    private final WishService wishService;
    private final DateUtils dateUtils;
    private final Mapper mapper;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(Principal principal) {
        return ResponseEntity.ok(mapper.convertUserToDto(userService.getUserByPrincipal(principal)));
    }

    @GetMapping("/employee")
    public ResponseEntity<EmployeeDTO> getUserEmployee(Principal principal) {
        Employee employee = employeeService.getEmployeeByPrincipal(principal);
        return ResponseEntity.ok(employee == null ? null : mapper.convertEmployeeToDto(employee));
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDTO>> getUserDepartments(Principal principal) {

        List<Department> departments = departmentService.getDepartmentsByPrincipal(principal);

        return ResponseEntity.ok(
                departments == null ? null
                        : departments.stream().map(mapper::convertDepartmentToDto).collect(Collectors.toList())
        );
    }

    @GetMapping("/statistic")
    public ResponseEntity<StatisticData> getStatisticData(
            @RequestParam Long departmentId,
            @RequestParam DatePeriodType periodType,
            @RequestParam(required = false) LocalDate dateStart,
            @RequestParam(required = false) LocalDate dateFinish,
            Principal principal
    ) {
        StatisticData res;
        if (periodType == DatePeriodType.CUSTOM) {
            res = paymentService.getStatisticData(departmentId, principal, StatType.STAT_OWN, dateStart, dateFinish);
        } else {
            DatePeriod datePeriod = dateUtils.getDatesOfPeriodType(periodType);
            res = paymentService.getStatisticData(departmentId, principal, StatType.STAT_OWN, datePeriod.getDateStart(), datePeriod.getDateFinish());
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/wishes")
    public ResponseEntity<List<ScheduleData>> getScheduleData(
            @RequestParam Long departmentId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate date,
            @RequestParam Integer pageIndex,
            @RequestParam Integer pageSize
    ) {
        Pagination pagination = new Pagination(pageIndex, pageSize);
        List<ScheduleData> result = new ArrayList<>(pagination.getPageSize());
        for (int i = 0; i < pagination.getPageSize(); i++) {
            List<DayData> dayDataList = dateUtils.getDayDataList(date, pagination, i);
            System.out.println(date);
            System.out.println(dayDataList);
            List<Employee> employees = employeeService.getEmployeesFromDepartment(departmentId);
            List<ScheduleRowData> rowDataList = new ArrayList<>();
            employees.stream()
                    .filter(Employee::getActive)
                    .forEach(employee -> rowDataList.add(ScheduleRowData.builder()
                        .employeeId(employee.getId())
                        .employeeName(employee.getName())
                        .wishes(wishService.getWishesForWeek(
                                                employee.getId(),
                                                departmentId,
                                                dayDataList.get(0).getDate().atStartOfDay(),
                                                dayDataList.get(6).getDate().atTime(23, 59)
                                        ).stream()
                                        .map(mapper::convertWishToDto).toList()
                        )
                        .build()));
            result.add(new ScheduleData(dayDataList, rowDataList));
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/wish")
    public ResponseEntity<WishDTO> createWish(@RequestBody WishDTO wishDTO) {
        Wish result = wishService.createWish(mapper.convertDtoToWish(wishDTO));
        return ResponseEntity.ok(result == null ? null : mapper.convertWishToDto(result));
    }

    @PutMapping("/wish")
    public ResponseEntity<Boolean> editWish(@RequestBody WishDTO wishDTO) {
        return ResponseEntity.ok(wishService.editWish(mapper.convertDtoToWish(wishDTO)));
    }

    @DeleteMapping("/wish/{id}")
    public ResponseEntity<Boolean> deleteWish(@PathVariable Long id) {
        return ResponseEntity.ok(wishService.deleteWish(id));
    }

    @PostMapping("/password")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {
        return ResponseEntity.ok(userService.changePassword(request.getPassword(), principal));
    }

}
