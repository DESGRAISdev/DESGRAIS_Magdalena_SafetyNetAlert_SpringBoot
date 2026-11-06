package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatarepositoryTest {

    private Datarepository datarepository;

    @BeforeEach
    void setUp() {
        datarepository = new Datarepository();
        datarepository.initData();
    }

    @Test
    void testGetters() {
        assertNotNull(datarepository.getPersons());
        assertNotNull(datarepository.getFirestations());
        assertNotNull(datarepository.getMedicalrecords());
    }

    @Test
    void testPersonOperations() {
        Person p = new Person();
        p.setFirstName("John");
        p.setLastName("Boyd");
        p.setAddress("1509 Culver St");
        p.setCity("Culver");
        p.setZip("97451");
        p.setPhone("841-874-6512");
        p.setEmail("jaboyd@email.com");

        datarepository.savePerson(p);
        assertFalse(datarepository.getPersons().isEmpty());

        Person update = new Person();
        update.setFirstName("John");
        update.setLastName("Boyd");
        update.setAddress("New Address");
        update.setCity("New City");
        update.setZip("12345");
        update.setPhone("000-000-0000");
        update.setEmail("new@email.com");

        Person updated = datarepository.updatePerson(update);
        assertNotNull(updated);
        assertEquals("New Address", updated.getAddress());

        Person unknown = new Person();
        unknown.setFirstName("Unknown");
        unknown.setLastName("User");
        assertNull(datarepository.updatePerson(unknown));

        boolean deleted = datarepository.deletePerson("John", "Boyd");
        assertTrue(deleted);

        boolean deleteNotFound = datarepository.deletePerson("Unknown", "User");
        assertFalse(deleteNotFound);
    }

    @Test
    void testFirestationOperations() {
        Firestation f = new Firestation();
        f.setAddress("1509 Culver St");
        f.setStation("3");

        datarepository.saveFirestation(f);
        assertFalse(datarepository.getFirestations().isEmpty());

        Firestation update = new Firestation();
        update.setAddress("1509 Culver St");
        update.setStation("4");

        Firestation updated = datarepository.updateFirestation(update);
        assertNotNull(updated);
        assertEquals("4", updated.getStation());

        Firestation updateNotFound = new Firestation();
        updateNotFound.setAddress("Unknown Address");
        updateNotFound.setStation("1");
        assertNull(datarepository.updateFirestation(updateNotFound));

        datarepository.deleteFirestation("1509 Culver St", "4");

        datarepository.saveFirestation(f);
        datarepository.deleteFirestation("1509 Culver St", null);

        datarepository.saveFirestation(f);
        datarepository.deleteFirestation(null, "3");
    }

    @Test
    void testMedicalrecordOperations() {
        Medicalrecord m = new Medicalrecord();
        m.setFirstName("John");
        m.setLastName("Boyd");
        m.setBirthDate("03/06/1984");
        m.setMedications(List.of("aznol:350mg"));
        m.setAllergies(List.of("nillacilan"));

        datarepository.saveMedicalrecord(m);
        assertFalse(datarepository.getMedicalrecords().isEmpty());

        Medicalrecord update = new Medicalrecord();
        update.setFirstName("John");
        update.setLastName("Boyd");
        update.setBirthDate("03/06/1984");
        update.setMedications(List.of("none"));
        update.setAllergies(List.of());

        Medicalrecord updated = datarepository.updateMedicalrecord(update);
        assertNotNull(updated);
        assertEquals("none", updated.getMedications().get(0));

        Medicalrecord updateNotFound = new Medicalrecord();
        updateNotFound.setFirstName("Unknown");
        updateNotFound.setLastName("User");
        assertNull(datarepository.updateMedicalrecord(updateNotFound));

        boolean deleted = datarepository.deleteMedicalrecord("John", "Boyd");
        assertTrue(deleted);
    }
}
