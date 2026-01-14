package com.govtech.restaurantpicker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.govtech.restaurantpicker.entity.Session;
import com.govtech.restaurantpicker.entity.User;
import com.govtech.restaurantpicker.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class SessionServiceTest {

    @Autowired
    private SessionService service;

    @Autowired
    private UserRepository userRepository;

    @Test
    void sessionCreationWithValidUserTest() {

        // Given
        User user = new User();
        user.setName("creator");
        user = userRepository.save(user);

        // When
        Session session = service.createSession(user.getId());

        // Then
        assertEquals("ACTIVE", session.getStatus());
        assertEquals(user.getId(), session.getCreatedBy());
    }
}
