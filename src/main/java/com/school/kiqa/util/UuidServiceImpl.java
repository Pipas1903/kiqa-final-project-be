package com.school.kiqa.util;

import com.school.kiqa.exception.InvalidHeaderException;
import com.school.kiqa.exception.notFound.SessionNotFoundException;
import com.school.kiqa.persistence.entity.SessionEntity;
import com.school.kiqa.persistence.repository.SessionRepository;
import com.school.kiqa.properties.UuidProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.school.kiqa.exception.ErrorMessageConstants.INVALID_HEADER;
import static com.school.kiqa.exception.ErrorMessageConstants.SESSION_NOT_FOUND_UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UuidServiceImpl implements UuidService {

    private final UuidProperties uuidProperties;
    private final SessionRepository sessionRepository;

    @Override
    public String generateUuid(String identifier) {

        if (!uuidProperties.getValue().equals(identifier)) {
            log.error("Given identifier - {} - doesn't match required one", identifier);
            throw new InvalidHeaderException(INVALID_HEADER);
        }

        final UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        log.info("Generated uuid for session");

        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setTokenUuid(uuidAsString);
        final var savedSession = sessionRepository.save(sessionEntity);
        log.info("Saved session to database with id {}", savedSession.getId());
        return uuidAsString;
    }

    @Override
    public Long verifyUuid(String uuid) {
        SessionEntity session = sessionRepository.findByUuid(uuid)
                .orElseThrow(() -> {
                    log.error("Session with uuid {} doesn't exist", uuid);
                    return new SessionNotFoundException(String.format(SESSION_NOT_FOUND_UUID, uuid));
                });
        log.info("Retrieving session id {}", session.getId());
        return session.getId();
    }

}
