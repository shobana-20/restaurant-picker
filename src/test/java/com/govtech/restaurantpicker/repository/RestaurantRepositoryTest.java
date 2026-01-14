package com.govtech.restaurantpicker.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.govtech.restaurantpicker.entity.RestaurantSubmission;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void findBySessionIdTest() {

        // Given
        RestaurantSubmission r1 = new RestaurantSubmission();
        r1.setSessionId(1L);
        r1.setUserId(10L);
        r1.setRestaurantName("Paradise Biryani");

        RestaurantSubmission r2 = new RestaurantSubmission();
        r2.setSessionId(1L);
        r2.setUserId(11L);
        r2.setRestaurantName("Dominos");

        RestaurantSubmission r3 = new RestaurantSubmission();
        r3.setSessionId(2L);
        r3.setUserId(12L);
        r3.setRestaurantName("KFC");

        restaurantRepository.saveAll(List.of(r1, r2, r3));

        // When
        List<RestaurantSubmission> results = restaurantRepository.findBySessionId(1L);

        // Then
        assertEquals(2, results.size());
    }
}
