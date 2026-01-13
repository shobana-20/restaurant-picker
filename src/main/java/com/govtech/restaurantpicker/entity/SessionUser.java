package com.govtech.restaurantpicker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "session_users")
@IdClass(SessionUserId.class)
public class SessionUser {

    @Id
    private Long sessionId;

    @Id
    private Long userId;

    public SessionUser() {}

    public SessionUser(Long sessionId, Long userId) {
        this.sessionId = sessionId;
        this.userId = userId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Long getUserId() {
        return userId;
    }
}
