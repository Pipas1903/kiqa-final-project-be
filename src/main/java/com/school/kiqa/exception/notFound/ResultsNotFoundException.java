package com.school.kiqa.exception.notFound;

import com.school.kiqa.exception.KiqaException;

public class ResultsNotFoundException extends KiqaException {
    public ResultsNotFoundException(String message) {
        super(message);
    }
}
