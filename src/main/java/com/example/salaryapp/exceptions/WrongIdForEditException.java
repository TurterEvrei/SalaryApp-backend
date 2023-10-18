package com.example.salaryapp.exceptions;

public class WrongIdForEditException extends RuntimeException {

    public <T> WrongIdForEditException(Long id, T obj) {
        super(String.format("No %s for edit with id: %d", obj.getClass().getSimpleName(), id));
    }
}
