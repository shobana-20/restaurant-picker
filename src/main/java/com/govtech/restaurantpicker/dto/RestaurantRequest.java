package com.govtech.restaurantpicker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String restaurantName;
}
