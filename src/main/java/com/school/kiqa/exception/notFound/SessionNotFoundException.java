package com.school.kiqa.exception.notFound;

import com.school.kiqa.exception.KiqaException;

public class SessionNotFoundException extends KiqaException {
    public SessionNotFoundException(String message) {
        super(message);
    }
}
