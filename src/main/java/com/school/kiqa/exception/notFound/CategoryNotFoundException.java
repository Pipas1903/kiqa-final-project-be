package com.school.kiqa.exception.notFound;

import com.school.kiqa.exception.KiqaException;

public class CategoryNotFoundException extends KiqaException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
