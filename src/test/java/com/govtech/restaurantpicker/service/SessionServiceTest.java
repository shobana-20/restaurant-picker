package com.govtech.restaurantpicker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.govtech.restaurantpicker.entity.Session;
import com.govtech.restaurantpicker.entity.User;
import com.govtech.restaurantpicker.repository.UserRepository;
import com.govtech.restaurantpicker.service.SessionService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class SessionServiceTest {

    @Autowired
    private SessionService service;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCreateSession() {
        // Arrange: create a valid user
        User user = new User();
        user.setName("creator");
        user = userRepository.save(user);

        // Act
        Session s = service.createSession(user.getId());

        // Assert
        assertEquals("ACTIVE", s.getStatus());
        assertEquals(user.getId(), s.getCreatedBy());
    }
}
