package com.govtech.restaurantpicker.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.govtech.restaurantpicker.dto.RestaurantRequest;
import com.govtech.restaurantpicker.entity.RestaurantSubmission;
import com.govtech.restaurantpicker.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void submitRestaurantTest() throws Exception {

        Long sessionId = 1L;

        RestaurantRequest request = new RestaurantRequest();
        request.setUserId(10L);
        request.setRestaurantName("Paradise Biryani");

        RestaurantSubmission submission = new RestaurantSubmission();
        submission.setId(100L);

        when(restaurantService.submit(
                eq(sessionId),
                eq(10L),
                eq("Paradise Biryani")))
                .thenReturn(submission);

        mockMvc.perform(post("/sessions/{sessionId}/restaurants", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void pickRandomRestaurantTest() throws Exception {

        Long sessionId = 1L;

        when(restaurantService.pickRandom(sessionId))
                .thenReturn("Paradise Biryani");

        mockMvc.perform(get("/sessions/{sessionId}/restaurants/pick", sessionId))
                .andExpect(status().isOk());
    }

    @Test
    void listRestaurantsTest() throws Exception {

        Long sessionId = 1L;

        when(restaurantService.getAllBySession(sessionId))
                .thenReturn(List.of(new RestaurantSubmission()));

        mockMvc.perform(get("/sessions/{sessionId}/restaurants", sessionId))
                .andExpect(status().isOk());
    }
}
