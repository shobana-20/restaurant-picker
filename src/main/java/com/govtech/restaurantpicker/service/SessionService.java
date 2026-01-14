package com.govtech.restaurantpicker.service;

import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;
import com.govtech.restaurantpicker.entity.RestaurantSubmission;
import com.govtech.restaurantpicker.entity.Session;
import com.govtech.restaurantpicker.entity.SessionUser;
import com.govtech.restaurantpicker.exception.BusinessException;
import com.govtech.restaurantpicker.repository.RestaurantRepository;
import com.govtech.restaurantpicker.repository.SessionRepository;
import com.govtech.restaurantpicker.repository.SessionUserRepository;
import com.govtech.restaurantpicker.repository.UserRepository;

/**
 * Service responsible for managing session operations.
 * A session represents a single lunch decision flow where:
 * - One user creates the session
 * - Other users may join
 * - Joined users can submit restaurant suggestions
 * - The creator ends the session and a restaurant is picked
 * All business rules related to sessions are added here.
 */
@Service
public class SessionService {

    private final SessionRepository sessionRepo;
    private final UserRepository userRepo;
    private final SessionUserRepository sessionUserRepo;
    private final RestaurantRepository restaurantRepository;
    private static final String STATUS_ENDED = "ENDED";

    public SessionService(SessionRepository sessionRepo,
            UserRepository userRepo,
            SessionUserRepository sessionUserRepo,
            RestaurantRepository restaurantRepository) {
        this.sessionRepo = sessionRepo;
        this.userRepo = userRepo;
        this.sessionUserRepo = sessionUserRepo;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Creates a new lunch session for the given user.
     * 
     * @param userId ID of the user creating the session
     * @return the created session in ACTIVE state
     * @throws BusinessException if the user does not exist
     */
    public Session createSession(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new BusinessException("User not allowed");
        }

        Session session = new Session();
        session.setCreatedBy(userId);
        session.setStatus("ACTIVE");

        Session saved = sessionRepo.save(session);

        // creator automatically joins the session
        sessionUserRepo.save(new SessionUser(saved.getId(), userId));

        return saved;
    }

    /**
     * Allows a user to join an existing session.
     *
     * @param sessionId ID of the session to join
     * @param userId    ID of the user joining the session
     * @throws BusinessException if:
     *                           - session does not exist
     *                           - user does not exist
     *                           - session is already ended
     *                           - user has already joined the session
     */
    public void joinSession(Long sessionId, Long userId) {

        Session session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new BusinessException("Session not found"));

        if (STATUS_ENDED.equals(session.getStatus())) {
            throw new BusinessException("Cannot join ended session");
        }

        if (!userRepo.existsById(userId)) {
            throw new BusinessException("Invalid user");
        }

        if (sessionUserRepo.existsBySessionIdAndUserId(sessionId, userId)) {
            throw new BusinessException("User already joined");
        }

        sessionUserRepo.save(new SessionUser(sessionId, userId));
    }

    /**
     * Ends an active session and selects a restaurant from submitted suggestions.
     * Only the session creator is allowed to end the session.
     * Once ended:
     * - The session status becomes ENDED
     * - A restaurant is picked exactly once
     * - No further submissions are allowed
     *
     * @param sessionId ID of the session to end
     * @param userId    ID of the user requesting to end the session
     * @return the updated session with picked restaurant
     * @throws BusinessException if:
     *                           - session does not exist
     *                           - user is not the session creator
     *                           - session is already ended
     *                           - no restaurants were submitted
     */
    public Session endSession(Long sessionId, Long userId) {

        Session session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new BusinessException("Session not found"));

        // Only creator can end
        if (!session.getCreatedBy().equals(userId)) {
            throw new BusinessException("Only creator can end session");
        }

        // Idempotent behavior
        if (STATUS_ENDED.equals(session.getStatus())) {
            return session;
        }

        session.setStatus(STATUS_ENDED);

        // Pick restaurant ONLY if any exist
        List<RestaurantSubmission> submissions = restaurantRepository.findBySessionId(sessionId);

        if (!submissions.isEmpty()) {
            String picked = submissions
                    .get(new Random().nextInt(submissions.size()))
                    .getRestaurantName();
            session.setPickedRestaurant(picked);
        }

        return sessionRepo.save(session);
    }

}
