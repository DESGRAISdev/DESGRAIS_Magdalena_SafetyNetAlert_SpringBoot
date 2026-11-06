package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.service.MedicalrecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicalrecordController.class)
class MedicalrecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalrecordService medicalrecordService;

    @Test
    void testCreateMedicalrecord() throws Exception {
        Medicalrecord record = new Medicalrecord();
        when(medicalrecordService.addMedicalrecord(any(Medicalrecord.class))).thenReturn(record);

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Boyd\",\"birthdate\":\"03/06/1984\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateMedicalrecord_Success() throws Exception {
        Medicalrecord record = new Medicalrecord();
        when(medicalrecordService.updateMedicalrecord(any(Medicalrecord.class))).thenReturn(record);

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Boyd\",\"birthdate\":\"03/06/1984\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateMedicalrecord_NotFound() throws Exception {
        when(medicalrecordService.updateMedicalrecord(any(Medicalrecord.class))).thenReturn(null);

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Unknown\",\"lastName\":\"User\",\"birthdate\":\"01/01/2000\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMedicalrecord_Success() throws Exception {
        when(medicalrecordService.deleteMedicalrecord("John", "Boyd")).thenReturn(true);

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "John")
                        .param("lastName", "Boyd"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteMedicalrecord_NotFound() throws Exception {
        when(medicalrecordService.deleteMedicalrecord("Unknown", "User")).thenReturn(false);

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "Unknown")
                        .param("lastName", "User"))
                .andExpect(status().isNotFound());
    }
}