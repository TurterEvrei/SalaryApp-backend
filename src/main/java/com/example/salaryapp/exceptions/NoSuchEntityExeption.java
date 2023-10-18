package com.example.salaryapp.exceptions;

public class NoSuchEntityExeption extends RuntimeException {

    public <T> NoSuchEntityExeption (Long id, T obj) {
        super(String.format("No %s with id: %d in database", obj.getClass().getSimpleName(), id));
    }

}
