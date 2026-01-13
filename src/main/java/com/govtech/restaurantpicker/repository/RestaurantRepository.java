package com.govtech.restaurantpicker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.govtech.restaurantpicker.entity.RestaurantSubmission;

import java.util.List;

public interface RestaurantRepository
        extends JpaRepository<RestaurantSubmission, Long> {

    List<RestaurantSubmission> findBySessionId(Long sessionId);

}
