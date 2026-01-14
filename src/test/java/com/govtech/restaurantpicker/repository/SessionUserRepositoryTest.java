package com.govtech.restaurantpicker.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.govtech.restaurantpicker.entity.SessionUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class SessionUserRepositoryTest {

    @Autowired
    private SessionUserRepository sessionUserRepository;

    @Test
    void existsBySessionIdAndUserIdTest() {

        // Given
        Long sessionId = 1L;
        Long userId = 10L;

        SessionUser sessionUser = new SessionUser(sessionId, userId);

        sessionUserRepository.save(sessionUser);

        // When & Then
        assertTrue(sessionUserRepository.existsBySessionIdAndUserId(sessionId, userId));
        assertFalse(sessionUserRepository.existsBySessionIdAndUserId(sessionId, 99L));
    }
}
