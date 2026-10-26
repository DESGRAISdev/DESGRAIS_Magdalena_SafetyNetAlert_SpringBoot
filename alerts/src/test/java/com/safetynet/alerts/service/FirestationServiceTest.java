package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.Datarepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    public void addFirestation () {

        Firestation newFirestation = new Firestation();
        newFirestation.setAddress("254 X St");
        newFirestation.setStation("3");

        when(dataRepository.saveFirestation(any(Firestation.class))).thenReturn(newFirestation);

        Firestation createdFirestation = firestationService.addFirestation(newFirestation);

        assertNotNull (createdFirestation);
        assertEquals ("254 X St", createdFirestation.getAddress());
        assertEquals("3",createdFirestation.getStation());
        verify (dataRepository, times(1)).saveFirestation(newFirestation);
    }

    @Test
    public void updateFirestation () {

        Firestation newFirestation = new Firestation();
        newFirestation.setAddress("254 X St");
        newFirestation.setStation("3");

        Firestation firestationToUpDate = new Firestation();
        firestationToUpDate.setAddress("276 X St");
        firestationToUpDate.setStation("3");

        when(dataRepository.updateFirestation(any(Firestation.class))).thenReturn(firestationToUpDate);

        Firestation updatedFirestation = firestationService.updateFirestation(firestationToUpDate);

        assertNotNull(updatedFirestation);
        assertEquals ("276 X St", updatedFirestation.getAddress());
        verify(dataRepository, times(1)).updateFirestation(firestationToUpDate);
    }
    @Test
    public void deleteFirestation (){
        String address = "1509 Culver St";
        String station = "3";

        when(dataRepository.deleteFirestation(address,station)).thenReturn(true);

        boolean isDeleted = firestationService.deleteFirestation(address,station);

        assertTrue (isDeleted);
        verify (dataRepository, times (1)).deleteFirestation(address,station);

    }
}
