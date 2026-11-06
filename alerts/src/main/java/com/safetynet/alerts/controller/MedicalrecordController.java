package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.service.MedicalrecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalrecordController {
    private static final Logger logger = LoggerFactory.getLogger(MedicalrecordController.class);

    private final MedicalrecordService medicalrecordService;

    @Autowired
    public MedicalrecordController(MedicalrecordService medicalrecordService) {
        this.medicalrecordService = medicalrecordService;
    }

    @PostMapping
    public ResponseEntity<Medicalrecord> createMedicalrecord(@RequestBody Medicalrecord medicalrecord) {
        logger.info("Requête reçue : POST /medicalRecord pour : {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        Medicalrecord created = medicalrecordService.addMedicalrecord(medicalrecord);

        logger.info("Réponse envoyée avec succès : dossier médical créé (statut 201)");
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping
    public ResponseEntity<Medicalrecord> updateMedicalrecord(@RequestBody Medicalrecord medicalrecord) {
        logger.info("Requête reçue : PUT /medicalRecord pour : {} {}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        Medicalrecord updated = medicalrecordService.updateMedicalrecord(medicalrecord);
        if (updated != null) {
            logger.info("Réponse envoyée avec succès : dossier médical mis à jour (statut 200)");
            return ResponseEntity.ok(updated);
        }

        logger.warn("Réponse envoyée : dossier médical introuvable pour la mise à jour (statut 404)");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

        @DeleteMapping
        public ResponseEntity<Void> deleteMedicalrecord(@RequestParam String firstName, @RequestParam String lastName) {
            logger.info("Requête reçue : DELETE /medicalRecord pour : {} {}", firstName, lastName);

            boolean deleted = medicalrecordService.deleteMedicalrecord(firstName, lastName);
            if (deleted) {
                logger.info("Réponse envoyée avec succès : dossier médical supprimé (statut 200)");
                return ResponseEntity.ok().build();
            }

            logger.warn("Réponse envoyée : impossible de supprimer, dossier médical introuvable (statut 404)");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
}
