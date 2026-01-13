package com.govtech.restaurantpicker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.govtech.restaurantpicker.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}