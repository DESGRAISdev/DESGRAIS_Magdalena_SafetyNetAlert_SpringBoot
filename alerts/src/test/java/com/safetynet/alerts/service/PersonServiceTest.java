package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.Datarepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class PersonServiceTest {

    @Mock
    private Datarepository dataRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    public void testAddPerson() {
        Person newPerson = new Person();
        newPerson.setFirstName("John");
        newPerson.setLastName("Doe");
        newPerson.setAddress("33 road");
        newPerson.setCity("Culver");
        newPerson.setZip("97451");
        newPerson.setPhone("33589647");
        newPerson.setEmail("johndoe@hotmail.com");

        when(dataRepository.savePerson(any(Person.class))).thenReturn(newPerson);

        Person createdPerson = personService.addPerson(newPerson);

        assertNotNull(createdPerson);
        assertEquals("John", createdPerson.getFirstName());
        assertEquals("Doe", createdPerson.getLastName());
        assertEquals("33 road", createdPerson.getAddress());
        assertEquals("Culver", createdPerson.getCity());
        verify(dataRepository, times(1)).savePerson(newPerson);
    }

    @Test
    public void updatePerson() {
        Person personToUpdate = new Person();
        personToUpdate.setLastName("Doe");

        Person expectedUpdatedPerson = new Person();
        expectedUpdatedPerson.setLastName("Smith");

        when(dataRepository.updatePerson(personToUpdate)).thenReturn(expectedUpdatedPerson);

        Person updatedPerson = personService.updatePerson(personToUpdate);

        assertNotNull(updatedPerson);
        assertEquals("Smith", updatedPerson.getLastName());
        verify(dataRepository, times(1)).updatePerson(personToUpdate);
    }

    @Test
    public void deletePerson() {
        String firstName = "John";
        String lastName = "Boyd";

        when(dataRepository.deletePerson(firstName, lastName)).thenReturn(true);

        boolean isDeleted = personService.deletePerson(firstName, lastName);

        assertTrue(isDeleted);
        verify(dataRepository, times(1)).deletePerson(firstName, lastName);

    }


}
