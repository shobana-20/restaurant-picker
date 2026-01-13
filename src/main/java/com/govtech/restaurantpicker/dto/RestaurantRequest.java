package com.govtech.restaurantpicker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RestaurantRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String restaurantName;

    public RestaurantRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}