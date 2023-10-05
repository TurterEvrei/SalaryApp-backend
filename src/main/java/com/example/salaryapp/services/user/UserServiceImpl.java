package com.example.salaryapp.services.user;

import com.example.salaryapp.entities.User;
import com.example.salaryapp.exceptions.WrongIdForEditException;
import com.example.salaryapp.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.NameAlreadyBoundException;
import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
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
        User storedUser = userRepo.findById(user.getId()).orElse(null);
        if (storedUser == null) {
            throw new WrongIdForEditException(user.getId(), user);
        }
        if (!storedUser.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        System.out.println(user.getEmployee().getUser().getId());
        return userRepo.save(user);
    }

    @Override
    public Boolean deleteUser(Long id) {
        try {
            userRepo.deleteById(id);
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

}
