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

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService service;

    public SessionController(SessionService service) {
        this.service = service;
    }

    @PostMapping
    public Session create(@RequestBody CreateSessionRequest request) {
        return service.createSession(request.getUserId());
    }

    @PostMapping("/{sessionId}/join")
    public void join(@PathVariable Long sessionId,
                     @RequestParam Long userId) {
        service.joinSession(sessionId, userId);
    }

    @PostMapping("/{sessionId}/end")
    public Session end(@PathVariable Long sessionId,
                       @RequestParam Long userId) {
        return service.endSession(sessionId, userId);
    }
}
