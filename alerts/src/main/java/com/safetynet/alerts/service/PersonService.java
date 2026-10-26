package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.Datarepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final Datarepository dataRepository;

    @Autowired
    public PersonService(Datarepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Person addPerson(Person person) {
        return dataRepository.savePerson(person);
    }

    public Person updatePerson(Person person) {
        return dataRepository.updatePerson(person);
    }

    public boolean deletePerson(String firstName, String lastName) {
        return dataRepository.deletePerson(firstName, lastName);
    }
}
