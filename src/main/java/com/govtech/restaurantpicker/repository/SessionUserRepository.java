package com.govtech.restaurantpicker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.govtech.restaurantpicker.entity.SessionUser;
import com.govtech.restaurantpicker.entity.SessionUserId;

public interface SessionUserRepository
        extends JpaRepository<SessionUser, SessionUserId> {

    boolean existsBySessionIdAndUserId(Long sessionId, Long userId);
}
