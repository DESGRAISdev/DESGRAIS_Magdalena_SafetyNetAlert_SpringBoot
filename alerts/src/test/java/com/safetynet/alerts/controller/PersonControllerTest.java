package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
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

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    void testAddPerson() throws Exception {
        Person person = new Person();
        when(personService.addPerson(any(Person.class))).thenReturn(person);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Boyd\",\"address\":\"1509 Culver St\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdatePerson_Success() throws Exception {
        Person person = new Person();
        when(personService.updatePerson(any(Person.class))).thenReturn(person);

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Boyd\",\"address\":\"1509 Culver St\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePerson_NotFound() throws Exception {
        when(personService.updatePerson(any(Person.class))).thenReturn(null);

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Unknown\",\"lastName\":\"User\",\"address\":\"Nowhere\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePerson_Success() throws Exception {
        when(personService.deletePerson("John", "Boyd")).thenReturn(true);

        mockMvc.perform(delete("/person")
                        .param("firstName", "John")
                        .param("lastName", "Boyd"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeletePerson_NotFound() throws Exception {
        when(personService.deletePerson("Unknown", "User")).thenReturn(false);

        mockMvc.perform(delete("/person")
                        .param("firstName", "Unknown")
                        .param("lastName", "User"))
                .andExpect(status().isNotFound());
    }
}
