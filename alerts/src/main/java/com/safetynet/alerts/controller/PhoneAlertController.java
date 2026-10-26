package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.FirestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



    @RestController
    public class PhoneAlertController {

        private static final Logger logger = LoggerFactory.getLogger(PhoneAlertController.class);
        private final FirestationService firestationService;

        @Autowired
        public PhoneAlertController(FirestationService firestationService) {
            this.firestationService = firestationService;
        }
        @GetMapping("/phoneAlert")
        public List<String> getPhoneAlertByFirestation(@RequestParam String firestation) {
            logger.info("Requête reçue pour l'endpoint /phoneAlert avec la caserne : {}", firestation);

            List<String> phoneNumbers = firestationService.getPhoneAlertByFirestationNumber(firestation);

            logger.info("Réponse envoyée pour /phoneAlert : {} numéros trouvés", phoneNumbers.size());
            return phoneNumbers;
        }
}
