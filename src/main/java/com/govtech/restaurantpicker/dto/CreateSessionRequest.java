package com.govtech.restaurantpicker.dto;

public class CreateSessionRequest {

    private Long userId;

    public CreateSessionRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
