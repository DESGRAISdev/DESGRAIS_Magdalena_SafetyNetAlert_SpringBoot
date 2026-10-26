package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.FirestationService;
import com.safetynet.alerts.dto.ChildAlertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChildAlertController {

    private final FirestationService firestationService;

    @Autowired
    public ChildAlertController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping("/childAlert")
    public ResponseEntity<ChildAlertDTO> getChildAlert(@RequestParam String address) {
        ChildAlertDTO result = firestationService.getChildAlertByAddress(address);

        if (result.getChildren().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
    }
}
