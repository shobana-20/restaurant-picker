package com.govtech.restaurantpicker.entity;

import java.io.Serializable;
import java.util.Objects;

public class SessionUserId implements Serializable {

    private Long sessionId;
    private Long userId;

    public SessionUserId() {}

    public SessionUserId(Long sessionId, Long userId) {
        this.sessionId = sessionId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionUserId)) return false;
        SessionUserId that = (SessionUserId) o;
        return Objects.equals(sessionId, that.sessionId)
                && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, userId);
    }
}
