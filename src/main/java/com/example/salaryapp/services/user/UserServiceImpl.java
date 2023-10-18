package com.example.salaryapp.services.user;

import com.example.salaryapp.entities.Employee;
import com.example.salaryapp.entities.User;
import com.example.salaryapp.exceptions.NoSuchEntityExeption;
import com.example.salaryapp.exceptions.WrongIdForEditException;
import com.example.salaryapp.repositories.EmployeeRepo;
import com.example.salaryapp.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.NameAlreadyBoundException;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final EmployeeRepo employeeRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("No user with id: " + id));
    }

    @Override
    public User getUserByPrincipal(Principal principal) {
        return userRepo.findByEmail(principal.getName())
                .orElseThrow(() ->
                        new UsernameNotFoundException("No user with email: " + principal.getName()));
    }

    @Override
    public User createUser(User user) throws NameAlreadyBoundException {
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new NameAlreadyBoundException(
                    String.format("User with email: %s already exist", user.getEmail()));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public User editUser(User user) {
        try {
            return preEditUser(user);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public Boolean editUsers(List<User> users) {
        try {
            users.forEach(this::preEditUser);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Boolean deleteUser(Long id) {
        try {
            User storedUser = userRepo.findById(id).orElseThrow(() ->
                    new NoSuchEntityExeption(id, User.class));
            Employee employee = storedUser.getEmployee();
            if (employee != null) {
                employee.setUser(null);
                storedUser.setEmployee(null);
                employeeRepo.save(employee);
            }
            userRepo.delete(storedUser);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean deleteUsers(List<User> users) {
        try {
            userRepo.deleteAll(users);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private User preEditUser(User user) {
        User storedUser = userRepo.findById(user.getId()).orElse(null);
        if (storedUser == null) {
            throw new WrongIdForEditException(user.getId(), user);
        }
        Employee oldEmployee = storedUser.getEmployee();
        Employee currentEmployee = user.getEmployee();

        if (!storedUser.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (currentEmployee != null) {
            User oldUser = currentEmployee.getUser();
            if (oldEmployee != null && !oldEmployee.equals(currentEmployee)) {
                oldEmployee.setUser(null);
                employeeRepo.save(oldEmployee);
            }
            if (oldUser != null && !oldUser.equals(user)) {
                oldUser.setEmployee(null);
                userRepo.save(oldUser);
            }
            currentEmployee.setUser(user);
        }
        return userRepo.save(user);
    }

}
