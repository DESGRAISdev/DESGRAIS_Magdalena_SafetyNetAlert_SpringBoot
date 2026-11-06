package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.FirestationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhoneAlertController.class)
class PhoneAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FirestationService firestationService;

    @Test
    void testGetPhoneAlertByFirestation() throws Exception {
        when(firestationService.getPhoneAlertByFirestationNumber("3")).thenReturn(List.of("841-874-6512", "841-874-6513"));

        mockMvc.perform(get("/phoneAlert").param("firestation", "3"))
                .andExpect(status().isOk());
    }
}
