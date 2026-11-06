package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.FirestationService;
import com.safetynet.alerts.dto.ChildAlertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChildAlertController {
    private static final Logger logger = LoggerFactory.getLogger(ChildAlertController.class);

    private final FirestationService firestationService;

    @Autowired
    public ChildAlertController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping("/childAlert")
    public ResponseEntity<ChildAlertDTO> getChildAlert(@RequestParam String address) {

        logger.info("Requête reçue : GET /childAlert avec l'adresse : {}", address);

        ChildAlertDTO result = firestationService.getChildAlertByAddress(address);

        if (result.getChildren().isEmpty()) {

            logger.info("Réponse envoyée : aucun enfant trouvé pour l'adresse : {}", address);
            return ResponseEntity.noContent().build();
        }


        logger.info("Réponse envoyée avec succès pour l'endpoint /childAlert");
        return ResponseEntity.ok(result);
    }
}
