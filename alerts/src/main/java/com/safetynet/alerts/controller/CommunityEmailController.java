package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommunityEmailController {

    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailController.class);
    private final PersonService personService;

    @Autowired
    public CommunityEmailController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmail(@RequestParam String city) {
        logger.info("Requête reçue pour l'endpoint /communityEmail avec la ville : {}", city);

        List<String> emails = personService.getCommunityEmail(city);

        logger.info("Réponse envoyée pour /communityEmail : {} e-mails trouvés", emails.size());
        return emails;
    }
}
