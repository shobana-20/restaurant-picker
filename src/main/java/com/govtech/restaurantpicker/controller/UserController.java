package com.govtech.restaurantpicker.controller;

import org.springframework.web.bind.annotation.*;

import com.govtech.restaurantpicker.entity.User;
import com.govtech.restaurantpicker.repository.UserRepository;
import java.util.List;

/**
 * REST controller handling APIs to manage user creation.
 *
 * Provides endpoints to:
 * - Create user
 * - Update user
 * - List all users
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Create a new user.
     *
     * @param user request containing user details
     * @return created user
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    /**
     * List users.
     *
     * @return List all users
     */
    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
