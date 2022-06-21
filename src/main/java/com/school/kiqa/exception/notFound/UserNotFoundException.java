package com.school.kiqa.exception.notFound;

import com.school.kiqa.exception.KiqaException;

public class UserNotFoundException extends KiqaException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
