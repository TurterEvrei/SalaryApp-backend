package com.example.salaryapp.repositories;

import com.example.salaryapp.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token, Long> {

    @Query(value = """
        select t from Token t inner join User u\s
        on t.user.id = u.id\s
        where u.id = :id and (t.expired = false or t.revoked = false)\s
        """)
    List<Token> findAllValidTokenByUserId(Long id);

    Optional<Token> findByToken(String token);

    @Query(value = """
        select t from Token t inner join User u\s
        on t.user.id = u.id\s
        where u.id = :id\s
        """)
    List<Token> findByUserId(Long id);

}
