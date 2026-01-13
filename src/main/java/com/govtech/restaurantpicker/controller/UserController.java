package com.govtech.restaurantpicker.controller;

import org.springframework.web.bind.annotation.*;

import com.govtech.restaurantpicker.entity.User;
import com.govtech.restaurantpicker.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping
    public java.util.List<User> getAll() {
        return userRepository.findAll();
    }
}
