package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.ChildDetailDTO;
import com.safetynet.alerts.service.FirestationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChildAlertController.class)
class ChildAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FirestationService firestationService;

    @Test
    void testGetChildAlert_Success() throws Exception {
        ChildAlertDTO dto = new ChildAlertDTO(
                List.of(new ChildDetailDTO("Tenley", "Boyd", 12)),
                Collections.emptyList()
        );

        when(firestationService.getChildAlertByAddress("1509 Culver St")).thenReturn(dto);

        mockMvc.perform(get("/childAlert").param("address", "1509 Culver St"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetChildAlert_NoChildren_ReturnsNoContent() throws Exception {
        ChildAlertDTO emptyDto = new ChildAlertDTO(Collections.emptyList(), Collections.emptyList());

        when(firestationService.getChildAlertByAddress("Unknown Address")).thenReturn(emptyDto);

        mockMvc.perform(get("/childAlert").param("address", "Unknown Address"))
                .andExpect(status().isNoContent());
    }
}
