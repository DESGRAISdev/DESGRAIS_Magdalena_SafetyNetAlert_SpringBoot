package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireDTO;
import com.safetynet.alerts.dto.FloodStationDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {

    @Mock
    private Datarepository dataRepository;

    @InjectMocks
    private FirestationService firestationService;

    @Test
    public void addFirestation() {
        Firestation newFirestation = new Firestation();
        newFirestation.setAddress("254 X St");
        newFirestation.setStation("3");

        when(dataRepository.saveFirestation(any(Firestation.class))).thenReturn(newFirestation);

        Firestation createdFirestation = firestationService.addFirestation(newFirestation);

        assertNotNull(createdFirestation);
        assertEquals("254 X St", createdFirestation.getAddress());
        assertEquals("3", createdFirestation.getStation());
        verify(dataRepository, times(1)).saveFirestation(newFirestation);
    }

    @Test
    public void updateFirestation() {
        Firestation newFirestation = new Firestation();
        newFirestation.setAddress("254 X St");
        newFirestation.setStation("3");

        Firestation firestationToUpDate = new Firestation();
        firestationToUpDate.setAddress("276 X St");
        firestationToUpDate.setStation("3");

        when(dataRepository.updateFirestation(any(Firestation.class))).thenReturn(firestationToUpDate);

        Firestation updatedFirestation = firestationService.updateFirestation(firestationToUpDate);

        assertNotNull(updatedFirestation);
        assertEquals("276 X St", updatedFirestation.getAddress());
        verify(dataRepository, times(1)).updateFirestation(firestationToUpDate);
    }

    @Test
    public void deleteFirestation() {
        String address = "1509 Culver St";
        String station = "3";

        when(dataRepository.deleteFirestation(address, station)).thenReturn(true);

        boolean isDeleted = firestationService.deleteFirestation(address, station);

        assertTrue(isDeleted);
        verify(dataRepository, times(1)).deleteFirestation(address, station);
    }

    
    @Test
    public void calculateAge_shouldReturnCorrectAge() {
        Medicalrecord record = new Medicalrecord();
        record.setBirthDate("10/06/2008");

        int age = record.calculateAge();

        assertEquals(18, age);
    }

    @Test
    public void getPersonsByStationNumber() {
        Firestation firestation = new Firestation();
        firestation.setAddress("1509 Culver St");
        firestation.setStation("3");

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");

        Medicalrecord medicalRecord = new Medicalrecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthDate("12/06/1999");

        when(dataRepository.getFirestations()).thenReturn(List.of(firestation));
        when(dataRepository.getPersons()).thenReturn(List.of(person));
        when(dataRepository.getMedicalrecords()).thenReturn(List.of(medicalRecord));

        var result = firestationService.getPersonsByStationNumber("3");
        assertNotNull(result);
        assertEquals(1, result.getPersons().size());
        assertEquals(1, result.getAdultCount());
    }

    @Test
    public void getChildAlertByAddress() {
        Person child = new Person();
        child.setFirstName("Jacob");
        child.setLastName("Boyd");
        child.setAddress("1509 Culver St");

        Person adult = new Person();
        adult.setFirstName("John");
        adult.setLastName("Boyd");
        adult.setAddress("1509 Culver St");

        Medicalrecord MrChild = new Medicalrecord();
        MrChild.setFirstName("Jacob");
        MrChild.setLastName("Boyd");
        MrChild.setBirthDate("10/06/2020");

        Medicalrecord MrAdult = new Medicalrecord();
        MrAdult.setFirstName("John");
        MrAdult.setLastName("Boyd");
        MrAdult.setBirthDate("10/06/1999");

        when(dataRepository.getPersons()).thenReturn(List.of(child, adult));
        when(dataRepository.getMedicalrecords()).thenReturn(List.of(MrChild, MrAdult));

        var result = firestationService.getChildAlertByAddress("1509 Culver St");

        assertNotNull(result);
        assertEquals(1, result.getChildren().size());
        assertEquals(1, result.getOtherFamilyMembers().size());
    }

    @Test
    public void getPhoneAlertByFirestationNumber() {
        Firestation firestation = new Firestation();
        firestation.setAddress("29 15th St");
        firestation.setStation("2");

        Person person = new Person();
        person.setPhone("223-2254");
        person.setAddress("29 15th St");
        person.setFirstName("John");
        person.setLastName("Doe");

        when(dataRepository.getFirestations()).thenReturn(Arrays.asList(firestation));
        when(dataRepository.getPersons()).thenReturn(Arrays.asList(person));

        List<String> result = firestationService.getPhoneAlertByFirestationNumber("2");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("223-2254"));
    }

    @Test
    public void getPersonsAndStationByAdress() {
        Firestation firestation = new Firestation();
        firestation.setStation("3");
        firestation.setAddress("1509 Culver St");

        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setPhone("223-2254");
        person1.setAddress("1509 Culver St");

        Medicalrecord MrAdult1 = new Medicalrecord();
        MrAdult1.setFirstName("John");
        MrAdult1.setLastName("Doe");
        MrAdult1.setBirthDate("10/06/1999");
        MrAdult1.setMedications(Arrays.asList("azoline:100mg"));
        MrAdult1.setAllergies(Arrays.asList("peanut"));

        Person person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Doe");
        person2.setPhone("223-3334");
        person2.setAddress("1509 Culver St");

        Medicalrecord MrAdult2 = new Medicalrecord();
        MrAdult2.setFirstName("Jane");
        MrAdult2.setLastName("Doe");
        MrAdult2.setBirthDate("10/09/1999");
        MrAdult2.setMedications(Arrays.asList());
        MrAdult2.setAllergies(Arrays.asList("nillacilan"));

        Person person3 = new Person();
        person3.setFirstName("John");
        person3.setLastName("Boyld");
        person3.setPhone("223-9994");
        person3.setAddress("29 15th St");

        when(dataRepository.getFirestations()).thenReturn(Arrays.asList(firestation));
        when(dataRepository.getPersons()).thenReturn(Arrays.asList(person1, person2, person3));
        when(dataRepository.getMedicalrecords()).thenReturn(Arrays.asList(MrAdult1, MrAdult2));

        FireDTO result = firestationService.getPersonsAndStationByAddress("1509 Culver St");

        assertNotNull(result);
        assertEquals("3", result.getStationNumber());
        assertEquals(2, result.getPersons().size());

        assertEquals("John", result.getPersons().get(0).getFirstName());
        assertEquals("223-2254", result.getPersons().get(0).getPhone());
    }

    @Test
    public void getFloodStations() {
        List<String> stationNumbers = List.of("3");

        Firestation firestation = new Firestation();
        firestation.setAddress("1509 Culver St");
        firestation.setStation("3");

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("1509 Culver St");
        person.setPhone("841-841");

        Medicalrecord medicalRecord = new Medicalrecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthDate("03/02/1993");
        medicalRecord.setMedications(List.of("paracetamol:100mg"));
        medicalRecord.setAllergies(List.of("none"));

        Person person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Doe");
        person2.setAddress("1509 Culver St");
        person2.setPhone("841-841");

        Medicalrecord medicalRecord2 = new Medicalrecord();
        medicalRecord2.setFirstName("Jane");
        medicalRecord2.setLastName("Doe");
        medicalRecord2.setBirthDate("03/02/1989");
        medicalRecord2.setMedications(List.of("paracetamol:100mg"));
        medicalRecord2.setAllergies(List.of("peanut"));

        when(dataRepository.getFirestations()).thenReturn(List.of(firestation));
        when(dataRepository.getPersons()).thenReturn(List.of(person, person2));
        when(dataRepository.getMedicalrecords()).thenReturn(List.of(medicalRecord, medicalRecord2));

        List<FloodStationDTO> result = firestationService.getFloodStations(stationNumbers);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1509 Culver St", result.get(0).getAddress());

        assertEquals(2, result.get(0).getPersons().size());
        assertEquals("John", result.get(0).getPersons().get(0).getFirstName());
        assertEquals("Jane", result.get(0).getPersons().get(1).getFirstName());
    }
}
