package com.example.salaryapp.repositories;

import com.example.salaryapp.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    Optional<Token> findFirstByToken(String token);

    @Query(value = """
        select t from Token t inner join User u\s
        on t.user.id = u.id\s
        where u.id = :id\s
        """)
    List<Token> findByUserId(Long id);

    @Query("select t from Token t join t.user u where u.id =:userId and t.revoked = true and t.expired = true ")
    List<Token> findBearerRevokedAndExpiredTokens(Long userId);

//    @Modifying
//    @Query("delete from Token t where t.user.id =:userId and t.expired = true and t.revoked = true ")
//    void deleteRevokedTokens(Long userId);

}
