package com.example.salaryapp.services.user;

import com.example.salaryapp.entities.Employee;
import com.example.salaryapp.entities.User;

import javax.naming.NameAlreadyBoundException;
import java.security.Principal;
import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User getUserById(Long id);
    User getUserByPrincipal(Principal principal);
    User createUser(User user) throws NameAlreadyBoundException;
    User editUser(User user);
    Boolean deleteUser(Long id);
    Boolean deleteUsers(List<User> users);

}
