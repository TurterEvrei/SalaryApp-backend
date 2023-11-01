package com.example.salaryapp.services.wish;

import com.example.salaryapp.domain.StatisticData;
import com.example.salaryapp.entities.Wish;
import com.example.salaryapp.entities.enums.StatType;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface WishService {

    List<Wish> getWishesForWeek(Long employeeId, Long departmentId, LocalDateTime start, LocalDateTime finish);
    Wish createWish(Wish wish);
    Boolean editWish(Wish wish);
    Boolean deleteWish(Long id);


}
