package com.school.kiqa.exception.alreadyExists;

import com.school.kiqa.exception.KiqaException;

public class PasswordMismatchException extends KiqaException {
    public PasswordMismatchException(String message) {
        super(message);
    }
}
