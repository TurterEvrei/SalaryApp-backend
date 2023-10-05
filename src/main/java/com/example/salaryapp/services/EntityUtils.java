package com.example.salaryapp.services;

import com.example.salaryapp.exceptions.WrongIdForEditException;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public class EntityUtils {

    public static <T> T editEntity(JpaRepository<T, Long> repo, T entity, Long id) {
        if (repo.findById(id).isEmpty()) {
            throw new WrongIdForEditException(id, entity);
        }
        return repo.save(entity);
    }

    public static <T> Boolean deleteEntity(JpaRepository<T, Long> repo, Long id) {
        try {
            repo.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static <T> Boolean deleteEntities(JpaRepository<T, Long> repo, List<T> entities) {
        try {
            repo.deleteAll(entities);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}
