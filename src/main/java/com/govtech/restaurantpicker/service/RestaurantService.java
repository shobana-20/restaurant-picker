package com.govtech.restaurantpicker.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.govtech.restaurantpicker.entity.RestaurantSubmission;
import com.govtech.restaurantpicker.entity.Session;
import com.govtech.restaurantpicker.exception.BusinessException;
import com.govtech.restaurantpicker.repository.RestaurantRepository;
import com.govtech.restaurantpicker.repository.SessionRepository;
import com.govtech.restaurantpicker.repository.SessionUserRepository;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final SessionUserRepository sessionUserRepository;
    private final SessionRepository sessionRepository;

    // ✅ Constructor injection
    public RestaurantService(RestaurantRepository restaurantRepository,
                             SessionUserRepository sessionUserRepository,
                             SessionRepository sessionRepository) {
        this.restaurantRepository = restaurantRepository;
        this.sessionUserRepository = sessionUserRepository;
        this.sessionRepository = sessionRepository;
    }

    // ✅ Submit restaurant (only ACTIVE session & joined users)
    public RestaurantSubmission submit(Long sessionId, Long userId, String restaurantName) {

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException("Session not found"));

        // ❌ Block submission after session ends
        if (!"ACTIVE".equals(session.getStatus())) {
            throw new BusinessException("Session is already ended. Cannot submit restaurants.");
        }

        if (!sessionUserRepository.existsBySessionIdAndUserId(sessionId, userId)) {
            throw new BusinessException("User has not joined this session");
        }

        RestaurantSubmission submission = new RestaurantSubmission();
        submission.setSessionId(sessionId);
        submission.setUserId(userId);
        submission.setRestaurantName(restaurantName);

        return restaurantRepository.save(submission);
    }

    // ✅ Pick random restaurant (ONLY after session ended)
    public String pickRandom(Long sessionId) {

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException("Session not found"));

        if (!"ENDED".equals(session.getStatus())) {
            throw new BusinessException("Session must be ended before picking restaurant");
        }

        List<RestaurantSubmission> submissions =
                restaurantRepository.findBySessionId(sessionId);

        if (submissions.isEmpty()) {
            throw new BusinessException("No restaurants submitted");
        }

        return submissions
                .get(new Random().nextInt(submissions.size()))
                .getRestaurantName();
    }

    public List<RestaurantSubmission> getAllBySession(Long sessionId) {

    Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new BusinessException("Session not found"));

    return restaurantRepository.findBySessionId(sessionId);
}
}
