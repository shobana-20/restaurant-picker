package com.govtech.restaurantpicker.controller;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.govtech.restaurantpicker.dto.RestaurantRequest;
import com.govtech.restaurantpicker.entity.RestaurantSubmission;
import com.govtech.restaurantpicker.service.RestaurantService;

/**
 * REST controller handling restaurant submissions for sessions.
 */
@RestController
@RequestMapping("/sessions/{sessionId}/restaurants")
public class RestaurantController {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    /**
     * Submits a restaurant suggestion for a session.
     *
     * @param sessionId ID of the session
     * @param request   request containing user ID and restaurant name
     * @return saved restaurant submission
     */
    @PostMapping
    public RestaurantSubmission submit(
            @PathVariable Long sessionId,
            @Valid @RequestBody RestaurantRequest request) {

        return service.submit(
                sessionId,
                request.getUserId(),
                request.getRestaurantName());
    }

    /**
     * Pick a random restaurant suggested from the session.
     *
     * @param sessionId ID of the session
     * @return random restaurant
     */
    @GetMapping("/pick")
    public String pick(@PathVariable Long sessionId) {
        return service.pickRandom(sessionId);
    }

    /**
     * List all the restaurant suggested from the session.
     *
     * @param sessionId ID of the session
     * @return all restaurant submitted
     */
    @GetMapping
    public List<RestaurantSubmission> list(@PathVariable Long sessionId) {
        return service.getAllBySession(sessionId);
    }
}