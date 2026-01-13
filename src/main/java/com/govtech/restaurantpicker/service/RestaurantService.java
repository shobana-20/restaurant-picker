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

/**
 * Service responsible for handling restaurant submissions
 * and for submission-related business rules.
 *
 * Submissions are allowed only:
 * - For ACTIVE sessions
 * - By users who have joined the session
 */
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final SessionUserRepository sessionUserRepository;
    private final SessionRepository sessionRepository;
    private static final String SESSION_NOT_FOUND = "Session not found";

    // Constructor injection
    public RestaurantService(RestaurantRepository restaurantRepository,
            SessionUserRepository sessionUserRepository,
            SessionRepository sessionRepository) {
        this.restaurantRepository = restaurantRepository;
        this.sessionUserRepository = sessionUserRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Submits a restaurant suggestion for a given session.
     *
     * @param sessionId      ID of the session
     * @param userId         ID of the user submitting the restaurant
     * @param restaurantName name of the restaurant
     * @return saved restaurant submission
     * @throws BusinessException if:
     *                           - session does not exist
     *                           - session is not ACTIVE
     *                           - user has not joined the session
     */
    public RestaurantSubmission submit(Long sessionId, Long userId, String restaurantName) {

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(SESSION_NOT_FOUND));

        // Block submission after session ends
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

    /**
     * Pick up a random restaurant after session closed.
     *
     * @param sessionId ID of the session
     * @return saved random restaurant
     * @throws BusinessException if:
     *                           - session does not exist
     *                           - session is ACTIVE
     *                           - no restaurant submitted in the session
     */
    public String pickRandom(Long sessionId) {

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(SESSION_NOT_FOUND));

        if (!"ENDED".equals(session.getStatus())) {
            throw new BusinessException("Session must be ended before picking restaurant");
        }

        List<RestaurantSubmission> submissions = restaurantRepository.findBySessionId(sessionId);

        if (submissions.isEmpty()) {
            throw new BusinessException("No restaurants submitted");
        }

        return submissions
                .get(new Random().nextInt(submissions.size()))
                .getRestaurantName();
    }

    /**
     * List all the restaurant after session closed.
     *
     * @param sessionId ID of the session
     * @return saved restaurant details
     * @throws BusinessException if:
     *                           - session does not exist
     *                           - session is ACTIVE
     */
    public List<RestaurantSubmission> getAllBySession(Long sessionId) {

        sessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(SESSION_NOT_FOUND));

        return restaurantRepository.findBySessionId(sessionId);
    }
}
