package com.govtech.restaurantpicker;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.restaurantpicker.entity.RestaurantSubmission;
import com.govtech.restaurantpicker.entity.Session;
import com.govtech.restaurantpicker.entity.User;
import com.govtech.restaurantpicker.exception.BusinessException;
import com.govtech.restaurantpicker.repository.RestaurantRepository;
import com.govtech.restaurantpicker.repository.SessionUserRepository;
import com.govtech.restaurantpicker.repository.UserRepository;
import com.govtech.restaurantpicker.service.RestaurantService;
import com.govtech.restaurantpicker.service.SessionService;

@SpringBootTest
@Transactional
class SessionFlowIntegrationTest {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private SessionService sessionService;

        @Autowired
        private RestaurantService restaurantService;

        @Autowired
        private SessionUserRepository sessionUserRepository;

        @Autowired
        private RestaurantRepository restaurantRepository;

        // Helper method (reduces duplication)
        private User createUser(String name) {
                User user = new User();
                user.setName(name);
                return userRepository.save(user);
        }

        // POSITIVE FLOW
        @Test
        void sessionFlowTest() {

                // Given
                User john = createUser("john");
                User jacob = createUser("jacob");

                // When: create session
                Session session = sessionService.createSession(john.getId());
                assertEquals("ACTIVE", session.getStatus());

                Long sessionId = session.getId();

                // When: join session
                sessionService.joinSession(sessionId, jacob.getId());
                assertTrue(sessionUserRepository.existsBySessionIdAndUserId(sessionId, jacob.getId()));

                // When: submit restaurant
                RestaurantSubmission submission = restaurantService.submit(sessionId, jacob.getId(),
                                "Paradise Biryani");

                assertNotNull(submission.getId());

                // When: end session
                Session endedSession = sessionService.endSession(sessionId, john.getId());

                // Then
                assertEquals("ENDED", endedSession.getStatus());
                assertEquals("Paradise Biryani", endedSession.getPickedRestaurant());

                List<RestaurantSubmission> restaurants = restaurantRepository.findBySessionId(sessionId);

                assertEquals(1, restaurants.size());
        }

        // NEGATIVE CASE
        @Test
        void submitRestaurantWithoutUserTest() {

                // Given
                User john = createUser("john");
                User susane = createUser("susane");

                Session session = sessionService.createSession(john.getId());

                // Then
                assertThrows(BusinessException.class,
                                () -> restaurantService.submit(session.getId(), susane.getId(), "KFC"));
        }

        // NEGATIVE CASE
        @Test
        void submitRestaurantAfterSessionClosedTest() {

                // Given
                User john = createUser("john");
                User jacob = createUser("jacob");

                Session session = sessionService.createSession(john.getId());
                sessionService.joinSession(session.getId(), jacob.getId());

                sessionService.endSession(session.getId(), john.getId());

                // Then
                assertThrows(BusinessException.class,
                                () -> restaurantService.submit(session.getId(), jacob.getId(), "Dominos"));
        }

        // NEGATIVE CASE
        @Test
        void closeSessionByOtherUserTest() {

                // Given
                User john = createUser("john");
                User jacob = createUser("jacob");

                Session session = sessionService.createSession(john.getId());

                // Then
                assertThrows(BusinessException.class, () -> sessionService.endSession(session.getId(), jacob.getId()));
        }
}
