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
        List<Person> mockPersonList = new ArrayList<>();

        when(dataRepository.savePerson(any(Person.class))).thenReturn(newPerson);

        Person createdPerson =personService.addPerson(newPerson);

        assertNotNull (createdPerson);
        assertEquals("John", createdPerson.getFirstName());
        assertEquals("Doe", createdPerson.getLastName());
        assertEquals("33 road", createdPerson.getAddress());
        assertEquals("Culver", createdPerson.getCity());
        verify(dataRepository,times(1)).savePerson(newPerson);

    }
}
