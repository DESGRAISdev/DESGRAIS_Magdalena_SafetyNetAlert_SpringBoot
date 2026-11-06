package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.FirestationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FirestationController.class)
class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FirestationService firestationService;

    @Test
    void testGetFirestationByStationNumber() throws Exception {
        // Optionnel : simuler le retour du service si nécessaire
        // when(firestationService.findByStationNumber(1)).thenReturn(Arrays.asList(...));

        mockMvc.perform(get("/firestation").param("stationNumber", "1"))
                .andExpect(status().isOk());
    }
}
