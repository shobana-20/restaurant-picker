package com.govtech.restaurantpicker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import com.govtech.restaurantpicker.entity.RestaurantSubmission;
import com.govtech.restaurantpicker.entity.Session;
import com.govtech.restaurantpicker.exception.BusinessException;
import com.govtech.restaurantpicker.repository.RestaurantRepository;
import com.govtech.restaurantpicker.repository.SessionRepository;
import com.govtech.restaurantpicker.repository.SessionUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

        @Mock
        private RestaurantRepository restaurantRepository;

        @Mock
        private SessionRepository sessionRepository;

        @Mock
        private SessionUserRepository sessionUserRepository;

        @InjectMocks
        private RestaurantService restaurantService;

        @Test
        void submitRestaurantTest() {

                Long sessionId = 1L;
                Long userId = 10L;

                Session session = new Session();
                session.setId(sessionId);
                session.setStatus("ACTIVE");

                when(sessionRepository.findById(sessionId))
                                .thenReturn(Optional.of(session));

                when(sessionUserRepository.existsBySessionIdAndUserId(sessionId, userId))
                                .thenReturn(true);

                RestaurantSubmission saved = new RestaurantSubmission();
                saved.setRestaurantName("Paradise Biryani");

                when(restaurantRepository.save(org.mockito.ArgumentMatchers.any()))
                                .thenReturn(saved);

                RestaurantSubmission result = restaurantService.submit(sessionId, userId, "Paradise Biryani");

                assertEquals("Paradise Biryani", result.getRestaurantName());
        }

        @Test
        void submitRestaurantSessionNotFoundTest() {

                when(sessionRepository.findById(1L))
                                .thenReturn(Optional.empty());

                assertThrows(BusinessException.class,
                                () -> restaurantService.submit(1L, 1L, "KFC"));
        }

        @Test
        void submitRestaurantSessionNotActiveTest() {

                Session session = new Session();
                session.setStatus("ENDED");

                when(sessionRepository.findById(1L))
                                .thenReturn(Optional.of(session));

                assertThrows(BusinessException.class,
                                () -> restaurantService.submit(1L, 1L, "KFC"));
        }

        @Test
        void submitRestaurantUserNotJoinedTest() {

                Session session = new Session();
                session.setStatus("ACTIVE");

                when(sessionRepository.findById(1L))
                                .thenReturn(Optional.of(session));

                when(sessionUserRepository.existsBySessionIdAndUserId(1L, 1L))
                                .thenReturn(false);

                assertThrows(BusinessException.class,
                                () -> restaurantService.submit(1L, 1L, "KFC"));
        }

        @Test
        void pickRandomRestaurantTest() {

                Session session = new Session();
                session.setStatus("ENDED");

                RestaurantSubmission submission = new RestaurantSubmission();
                submission.setRestaurantName("Dominos");

                when(sessionRepository.findById(1L))
                                .thenReturn(Optional.of(session));

                when(restaurantRepository.findBySessionId(1L))
                                .thenReturn(List.of(submission));

                String picked = restaurantService.pickRandom(1L);

                assertEquals("Dominos", picked);
        }

        @Test
        void pickRandomSessionNotEndedTest() {

                Session session = new Session();
                session.setStatus("ACTIVE");

                when(sessionRepository.findById(1L))
                                .thenReturn(Optional.of(session));

                assertThrows(BusinessException.class,
                                () -> restaurantService.pickRandom(1L));
        }

        @Test
        void pickRandomNoRestaurantsTest() {

                Session session = new Session();
                session.setStatus("ENDED");

                when(sessionRepository.findById(1L))
                                .thenReturn(Optional.of(session));

                when(restaurantRepository.findBySessionId(1L))
                                .thenReturn(List.of());

                assertThrows(BusinessException.class,
                                () -> restaurantService.pickRandom(1L));
        }
}
