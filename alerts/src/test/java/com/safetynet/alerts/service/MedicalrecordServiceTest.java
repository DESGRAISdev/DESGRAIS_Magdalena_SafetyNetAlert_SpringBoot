package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.repository.Datarepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalrecordServiceTest {

    @Mock
    private Datarepository dataRepository;

    @InjectMocks
    private MedicalrecordService medicalrecordService;

    @Test

    public void addMedicalRecord () {
        Medicalrecord newMedicalRecord = new Medicalrecord();
        newMedicalRecord.setLastName("Doe");

        when(dataRepository.saveMedicalrecord(any(Medicalrecord.class))).thenReturn(newMedicalRecord);

        Medicalrecord savedMedicalRecord = medicalrecordService.addMedicalrecord(newMedicalRecord);

        assertNotNull (savedMedicalRecord);
        assertEquals ("Doe", savedMedicalRecord.getLastName());
        verify ( dataRepository, times(1)).saveMedicalrecord(newMedicalRecord);
    }

    @Test
    public void updateMedicalRecord () {

        Medicalrecord existingRecord = new Medicalrecord();
        existingRecord.setFirstName("John");
        existingRecord.setLastName("Boyd");
        existingRecord.setAllergies(List.of("oldAllergy"));

        when(dataRepository.getMedicalrecords()).thenReturn(List.of(existingRecord));

        Medicalrecord medicalRecordToUpDate = new Medicalrecord();
        medicalRecordToUpDate.setFirstName("John");
        medicalRecordToUpDate.setLastName("Boyd");
        medicalRecordToUpDate.setAllergies(List.of("peanut"));

        Medicalrecord updatedMedicalRecord = medicalrecordService.updateMedicalrecord(medicalRecordToUpDate);

        assertEquals("John", updatedMedicalRecord.getFirstName());
        assertEquals("Boyd", updatedMedicalRecord.getLastName());
        assertEquals(List.of("peanut"), updatedMedicalRecord.getAllergies());
        verify(dataRepository, times(1)).getMedicalrecords();
    }

    @Test
    public void deleteMedicalRecord () {
String firstName = "John";
String lastName = "Boyd";

when(dataRepository.deleteMedicalrecord(firstName,lastName)).thenReturn(true);

boolean isDeleted = medicalrecordService.deleteMedicalrecord(firstName,lastName);

assertTrue (isDeleted);
verify(dataRepository,times(1)).deleteMedicalrecord(firstName,lastName);
    }

}
