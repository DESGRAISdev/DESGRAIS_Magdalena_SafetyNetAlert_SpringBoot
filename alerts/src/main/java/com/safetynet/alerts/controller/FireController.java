package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireDTO;
import com.safetynet.alerts.service.FirestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FireController {

    @Autowired
    private FirestationService firestationService;

    @GetMapping("/fire")
    public FireDTO getFireByAddress(@RequestParam String address) {
        return firestationService.getPersonsAndStationByAddress(address);
    }
}
