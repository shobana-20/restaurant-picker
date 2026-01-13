package com.govtech.restaurantpicker.service;

import java.util.List;
import java.util.Random;


import org.springframework.stereotype.Service;

import com.govtech.restaurantpicker.entity.RestaurantSubmission;
import com.govtech.restaurantpicker.entity.Session;
import com.govtech.restaurantpicker.entity.SessionUser;
import com.govtech.restaurantpicker.repository.RestaurantRepository;
import com.govtech.restaurantpicker.repository.SessionRepository;
import com.govtech.restaurantpicker.repository.SessionUserRepository;
import com.govtech.restaurantpicker.repository.UserRepository;

@Service
public class SessionService {

    private final SessionRepository sessionRepo;
    private final UserRepository userRepo;
    private final SessionUserRepository sessionUserRepo;
    private final RestaurantRepository restaurantRepository;

    public SessionService(SessionRepository sessionRepo,
                      UserRepository userRepo,
                      SessionUserRepository sessionUserRepo,
                      RestaurantRepository restaurantRepository) {
    this.sessionRepo = sessionRepo;
    this.userRepo = userRepo;
    this.sessionUserRepo = sessionUserRepo;
    this.restaurantRepository = restaurantRepository;
}

    public Session createSession(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new RuntimeException("User not allowed");
        }

        Session session = new Session();
        session.setCreatedBy(userId);
        session.setStatus("ACTIVE");

        Session saved = sessionRepo.save(session);

        // creator automatically joins the session
        sessionUserRepo.save(new SessionUser(saved.getId(), userId));

        return saved;
    }

    public void joinSession(Long sessionId, Long userId) {

        Session session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if ("ENDED".equals(session.getStatus())) {
            throw new RuntimeException("Cannot join ended session");
        }

        if (!userRepo.existsById(userId)) {
            throw new RuntimeException("Invalid user");
        }

        if (sessionUserRepo.existsBySessionIdAndUserId(sessionId, userId)) {
            throw new RuntimeException("User already joined");
        }

        sessionUserRepo.save(new SessionUser(sessionId, userId));
    }

 public Session endSession(Long sessionId, Long userId) {

    Session session = sessionRepo.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));

    if (!session.getCreatedBy().equals(userId)) {
        throw new RuntimeException("Only creator can end session");
    }

    // Idempotent behavior
    if ("ENDED".equals(session.getStatus())) {
        return session;
    }

    List<RestaurantSubmission> submissions =
            restaurantRepository.findBySessionId(sessionId);

    if (submissions.isEmpty()) {
        throw new RuntimeException("No restaurants submitted");
    }

    // ✅ Random selection in service layer
    int randomIndex = new Random().nextInt(submissions.size());
    String pickedRestaurant =
            submissions.get(randomIndex).getRestaurantName();

    session.setPickedRestaurant(pickedRestaurant);
    session.setStatus("ENDED");

    return sessionRepo.save(session);
}

}
