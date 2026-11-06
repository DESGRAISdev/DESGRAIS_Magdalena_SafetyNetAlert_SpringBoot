package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireDTO;
import com.safetynet.alerts.service.FirestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FireController {
    private static final Logger logger = LoggerFactory.getLogger(FireController.class);
    @Autowired
    private FirestationService firestationService;

    @GetMapping("/fire")
    public FireDTO getFireByAddress(@RequestParam String address) {

        logger.info("Requête reçue : GET /fire avec l'adresse : {}", address);

        FireDTO result = firestationService.getPersonsAndStationByAddress(address);

        logger.info("Réponse envoyée avec succès pour l'endpoint /fire");

        return result;
    }
}
