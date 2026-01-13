package com.govtech.restaurantpicker.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.govtech.restaurantpicker.dto.CreateSessionRequest;
import com.govtech.restaurantpicker.entity.Session;
import com.govtech.restaurantpicker.service.SessionService;

/**
 * REST controller exposing APIs to manage lunch sessions.
 *
 * Provides endpoints to:
 * - Create sessions
 * - Join sessions
 * - End sessions
 */
@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService service;

    public SessionController(SessionService service) {
        this.service = service;
    }

    /**
     * Creates a new lunch session.
     *
     * @param request request containing creator user ID
     * @return created session
     */
    @PostMapping
    public Session create(@RequestBody CreateSessionRequest request) {
        return service.createSession(request.getUserId());
    }

    /**
     * Allows a user to join an existing session.
     *
     * @param sessionId ID of the session
     * @param userId    ID of the user joining
     */
    @PostMapping("/{sessionId}/join")
    public void join(@PathVariable Long sessionId,
            @RequestParam Long userId) {
        service.joinSession(sessionId, userId);
    }

    /**
     * Ends a session and selects the final restaurant.
     *
     * Only the session creator is allowed to perform this action.
     *
     * @param sessionId ID of the session
     * @param userId    ID of the creator ending the session
     * @return ended session with picked restaurant
     */
    @PostMapping("/{sessionId}/end")
    public Session end(@PathVariable Long sessionId,
            @RequestParam Long userId) {
        return service.endSession(sessionId, userId);
    }
}
