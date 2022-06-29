package com.school.kiqa.util;

import com.school.kiqa.command.dto.auth.PrincipalDto;
import com.school.kiqa.persistence.entity.SessionEntity;

public interface UuidService {
    SessionEntity createSession(String uuid);

    String generateUuid(String identifier);

    Long verifyUuid(String uuid);
}
