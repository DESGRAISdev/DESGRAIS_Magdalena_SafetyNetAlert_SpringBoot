package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.service.MedicalrecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalrecordController {

    private final MedicalrecordService medicalrecordService;

    @Autowired
    public MedicalrecordController(MedicalrecordService medicalrecordService) {
        this.medicalrecordService = medicalrecordService;
    }

    @PostMapping
    public ResponseEntity<Medicalrecord> createMedicalrecord(@RequestBody Medicalrecord medicalrecord) {
        Medicalrecord created = medicalrecordService.addMedicalrecord(medicalrecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping
    public ResponseEntity<Medicalrecord> updateMedicalrecord(@RequestBody Medicalrecord medicalrecord) {
        Medicalrecord updated = medicalrecordService.updateMedicalrecord(medicalrecord);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMedicalrecord(@RequestParam String firstName, @RequestParam String lastName) {
        boolean deleted = medicalrecordService.deleteMedicalrecord(firstName, lastName);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
