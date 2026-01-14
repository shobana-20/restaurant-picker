package com.govtech.restaurantpicker.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.govtech.restaurantpicker.dto.CreateSessionRequest;
import com.govtech.restaurantpicker.entity.Session;
import com.govtech.restaurantpicker.service.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SessionService sessionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createSessionTest() throws Exception {

        CreateSessionRequest request = new CreateSessionRequest();
        request.setUserId(1L);

        Session session = new Session();
        session.setId(10L);

        when(sessionService.createSession(1L))
                .thenReturn(session);

        mockMvc.perform(post("/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void joinSessionTest() throws Exception {

        Long sessionId = 10L;
        Long userId = 2L;

        doNothing().when(sessionService)
                .joinSession(sessionId, userId);

        mockMvc.perform(post("/sessions/{sessionId}/join", sessionId)
                .param("userId", userId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void endSessionTest() throws Exception {

        Long sessionId = 10L;
        Long userId = 1L;

        Session endedSession = new Session();
        endedSession.setStatus("ENDED");

        when(sessionService.endSession(sessionId, userId))
                .thenReturn(endedSession);

        mockMvc.perform(post("/sessions/{sessionId}/end", sessionId)
                .param("userId", userId.toString()))
                .andExpect(status().isOk());
    }
}
