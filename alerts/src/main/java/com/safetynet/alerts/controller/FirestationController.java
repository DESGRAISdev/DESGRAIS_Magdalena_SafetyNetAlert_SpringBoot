package com.safetynet.alerts.controller;



import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationService;
import com.safetynet.alerts.dto.FirestationPersonsdto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/firestation")
public class FirestationController {
    private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);
    private final FirestationService firestationService;

    @Autowired
    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @PostMapping
    public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
        logger.info("Requête reçue : POST /firestation");

        Firestation created = firestationService.addFirestation(firestation);

        logger.info("Réponse envoyée avec succès : caserne créée (statut 201)");
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation) {
        logger.info("Requête reçue : PUT /firestation");

        Firestation updated = firestationService.updateFirestation(firestation);
        if (updated != null) {
            logger.info("Réponse envoyée avec succès : caserne mise à jour (statut 200)");
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }

        logger.warn("Réponse envoyée : caserne introuvable pour la mise à jour (statut 404)");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFirestation(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String station) {
        logger.info("Requête reçue : DELETE /firestation avec address = {} et station = {}", address, station);

        boolean isDeleted = firestationService.deleteFirestation(address, station);
        if (isDeleted) {
            logger.info("Réponse envoyée avec succès : caserne supprimée (statut 204)");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.warn("Réponse envoyée : impossible de supprimer, caserne introuvable (statut 404)");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<FirestationPersonsdto> getPersonsByStationNumber(@RequestParam String stationNumber) {
        logger.info("Requête reçue : GET /firestation avec stationNumber = {}", stationNumber);

        FirestationPersonsdto result = firestationService.getPersonsByStationNumber(stationNumber);

        logger.info("Réponse envoyée avec succès pour l'endpoint GET /firestation");
        return ResponseEntity.ok(result);
    }



}


