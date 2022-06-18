package com.school.kiqa.exception;

public class UserAlreadyExistsException extends AlreadyExistsException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
