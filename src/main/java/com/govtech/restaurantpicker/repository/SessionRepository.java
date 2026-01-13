package com.govtech.restaurantpicker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.govtech.restaurantpicker.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
