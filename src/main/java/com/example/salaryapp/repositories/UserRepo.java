package com.example.salaryapp.repositories;

import com.example.salaryapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
