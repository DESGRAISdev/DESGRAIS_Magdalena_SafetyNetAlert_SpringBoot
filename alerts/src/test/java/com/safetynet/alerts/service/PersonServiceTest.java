package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.Datarepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
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
@Test
public void getCommunityEmail () {

        Person p1 = new Person();
        p1.setCity("Culver");
        p1.setEmail("p1@gmail.com");

        Person p2 = new Person();
        p2.setCity("New York");
        p2.setEmail("p2@gmail.com");

        Person p3 = new Person();
        p3.setCity("Culver");
        p3.setEmail("p3@gmail.com");

        when (dataRepository.getPersons()).thenReturn(Arrays.asList(p1,p2,p3));

        List < String> emails = personService.getCommunityEmail("Culver");

        assertNotNull (emails);
        assertEquals(2, emails.size());
        assertTrue(emails.contains("p1@gmail.com"));
        assertTrue(emails.contains("p3@gmail.com"));
        assertFalse(emails.contains("p2@gmail.com"));
}
@Test
public void getCommunityEmail_UnknownCity () {
        Person p1 = new Person();
        p1.setEmail("p1@gmail.com");
        p1.setCity("Culver");
        
    when (dataRepository.getPersons()).thenReturn(Arrays.asList(p1));
    List<String> emails = personService.getCommunityEmail("UnknownCity");

    assertNotNull(emails);
    assertTrue(emails.isEmpty());

}
}
