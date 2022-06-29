package com.school.kiqa.util;

public interface UuidService {
    String generateUuid(String identifier);

    Long verifyUuid(String uuid);
}
