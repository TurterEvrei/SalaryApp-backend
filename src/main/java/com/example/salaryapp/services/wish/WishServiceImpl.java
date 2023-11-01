package com.example.salaryapp.services.wish;

import com.example.salaryapp.domain.StatisticData;
import com.example.salaryapp.entities.Employee;
import com.example.salaryapp.entities.User;
import com.example.salaryapp.entities.Wish;
import com.example.salaryapp.entities.enums.StatType;
import com.example.salaryapp.exceptions.WrongIdForEditException;
import com.example.salaryapp.repositories.EmployeeRepo;
import com.example.salaryapp.repositories.UserRepo;
import com.example.salaryapp.repositories.WishRepo;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

    private final WishRepo wishRepo;

    @Override
    public List<Wish> getWishesForWeek(Long employeeId, Long departmentId, LocalDateTime start, LocalDateTime finish) {
        return wishRepo.findWishesByEmployee_IdAndDepartment_IdAndDateTimeBetween(employeeId, departmentId, start, finish);
    }

    @Override
    public Wish createWish(Wish wish) {
        try {
            if (wishRepo.findWishByEmployee_IdAndDateTimeBetween(
                    wish.getEmployee().getId(),
                    wish.getDateTime().withHour(0),
                    wish.getDateTime().withHour(23)).isPresent()) {
                throw new RuntimeException("Wish already exists");
            }
            return wishRepo.save(wish);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public Boolean editWish(Wish wish) {
        try {
            if (!wishRepo.existsById(wish.getId())) {
                throw new WrongIdForEditException(wish.getId(), Wish.class);
            }
            wishRepo.save(wish);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Boolean deleteWish(Long id) {
        try {
            wishRepo.deleteById(id);
            if (wishRepo.existsById(id)) {
                throw new EntityExistsException(String.format("Error while deleting Wish with id: %d", id));
            }
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
