package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonController {
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        logger.info("Requête reçue : POST /person pour : {} {}", person.getFirstName(), person.getLastName());

        Person createdPerson = personService.addPerson(person);

        logger.info("Réponse envoyée avec succès : personne créée (statut 201)");
        return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        logger.info("Requête reçue : PUT /person pour : {} {}", person.getFirstName(), person.getLastName());

        Person updatedPerson = personService.updatePerson(person);
        if (updatedPerson != null) {
            logger.info("Réponse envoyée avec succès : personne mise à jour (statut 200)");
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        } else {
            logger.warn("Réponse envoyée : personne introuvable pour la mise à jour (statut 404)");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("Requête reçue : DELETE /person pour : {} {}", firstName, lastName);

        boolean deleted = personService.deletePerson(firstName, lastName);
        if (deleted) {
            logger.info("Réponse envoyée avec succès : personne supprimée (statut 204)");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.warn("Réponse envoyée : impossible de supprimer, personne introuvable (statut 404)");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
