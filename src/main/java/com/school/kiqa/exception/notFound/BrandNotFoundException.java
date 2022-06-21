package com.school.kiqa.exception.notFound;

import com.school.kiqa.exception.KiqaException;

public class BrandNotFoundException extends KiqaException {
    public BrandNotFoundException(String message) {
        super(message);
    }
}
