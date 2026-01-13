package com.govtech.restaurantpicker.controller;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.govtech.restaurantpicker.dto.RestaurantRequest;
import com.govtech.restaurantpicker.entity.RestaurantSubmission;
import com.govtech.restaurantpicker.service.RestaurantService;

@RestController
@RequestMapping("/sessions/{sessionId}/restaurants")
public class RestaurantController {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @PostMapping
    public RestaurantSubmission submit(
            @PathVariable Long sessionId,
            @Valid @RequestBody RestaurantRequest request) {

        return service.submit(
                sessionId,
                request.getUserId(),
                request.getRestaurantName()
        );
    }

    @GetMapping("/pick")
    public String pick(@PathVariable Long sessionId) {
        return service.pickRandom(sessionId);
    }

    @GetMapping
public List<RestaurantSubmission> list(@PathVariable Long sessionId) {
    return service.getAllBySession(sessionId);
}
}