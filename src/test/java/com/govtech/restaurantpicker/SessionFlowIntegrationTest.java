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
import com.govtech.restaurantpicker.repository.SessionRepository;
import com.govtech.restaurantpicker.repository.SessionUserRepository;
import com.govtech.restaurantpicker.repository.UserRepository;
import com.govtech.restaurantpicker.service.RestaurantService;
import com.govtech.restaurantpicker.service.SessionService;

@SpringBootTest
@Transactional
class SessionFlowIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionService sessionService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    SessionUserRepository sessionUserRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    void fullRestaurantPickerFlow_shouldWorkCorrectly() {

        // STEP 1 Create users
        User john = userRepository.save(new User("john"));
        User jacob = userRepository.save(new User("jacob"));
        User susane = userRepository.save(new User("susane"));

        assertNotNull(john.getId());
        assertNotNull(jacob.getId());
        assertNotNull(susane.getId());

        // STEP 2️ Create session by john
        Session session = sessionService.createSession(john.getId());
        assertEquals("ACTIVE", session.getStatus());

        Long sessionId = session.getId();

        // STEP 3️ jacob joins session
        sessionService.joinSession(sessionId, jacob.getId());
        assertTrue(sessionUserRepository.existsBySessionIdAndUserId(sessionId, jacob.getId()));

        // STEP 4️ jacob submits restaurant
        RestaurantSubmission submission =
                restaurantService.submit(sessionId, jacob.getId(), "Paradise Biryani");

        assertNotNull(submission.getId());

        // STEP 5️ End session by john
        Session endedSession = sessionService.endSession(sessionId, john.getId());

        assertEquals("ENDED", endedSession.getStatus());
        assertNotNull(endedSession.getPickedRestaurant());

        // STEP 6️ Get all restaurants
        List<RestaurantSubmission> restaurants =
                restaurantRepository.findBySessionId(sessionId);

        assertEquals(1, restaurants.size());

        // STEP 7️ Pick random restaurant
        String picked = endedSession.getPickedRestaurant();
        assertEquals("Paradise Biryani", picked);

        
        assertThrows(BusinessException.class, () ->
                restaurantService.submit(sessionId, susane.getId(), "KFC"));

       
        assertThrows(BusinessException.class, () ->
                restaurantService.submit(sessionId, jacob.getId(), "Dominos"));
    }
}
