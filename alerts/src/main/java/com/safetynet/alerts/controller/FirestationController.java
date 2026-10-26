package com.safetynet.alerts.controller;



import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationService;
import com.safetynet.alerts.dto.FirestationPersonsdto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/firestation")
public class FirestationController {
    private final FirestationService firestationService;

    @Autowired
    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @PostMapping
    public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
        Firestation created = firestationService.addFirestation(firestation);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation) {
        Firestation updated = firestationService.updateFirestation(firestation);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFirestation(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String station) {
        boolean isDeleted = firestationService.deleteFirestation(address, station);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<FirestationPersonsdto> getPersonsByStationNumber(@RequestParam String stationNumber) {
        com.safetynet.alerts.dto.FirestationPersonsdto result = firestationService.getPersonsByStationNumber(stationNumber);
        return ResponseEntity.ok(result);
    }

}


