package com.school.kiqa.exception.authExceptions;

import com.school.kiqa.exception.KiqaException;

public class WrongCredentialsException extends KiqaException {
    public WrongCredentialsException(String message) {
        super(message);
    }
}
