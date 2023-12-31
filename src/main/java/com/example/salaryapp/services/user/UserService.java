package com.example.salaryapp.services.user;

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
    Boolean editUsers(List<User> users);
    Boolean deleteUser(Long id);
    Boolean deleteUsers(List<User> users);
    Boolean changePassword(String password, Principal principal);

}
