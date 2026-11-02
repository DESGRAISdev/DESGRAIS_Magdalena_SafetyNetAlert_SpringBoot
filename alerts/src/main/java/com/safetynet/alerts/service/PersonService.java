package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FirePersonDTO;
import com.safetynet.alerts.dto.PersonInfoLastNameDTO;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.Datarepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



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

    public List<String> getCommunityEmail(String city) {
        return dataRepository.getPersons().stream()
                .filter(p -> p.getCity() != null && p.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .filter(email -> email != null && !email.isEmpty())
                .distinct()
                .toList();
    }

    public List<PersonInfoLastNameDTO> getPersonInfoByLastName(String lastName) {
        List<PersonInfoLastNameDTO> result = new ArrayList<>();

        List<Person> matchedPersons = dataRepository.getPersons().stream()
                .filter(p -> p.getLastName() != null && p.getLastName().equalsIgnoreCase(lastName))
                .toList();

        for (Person person : matchedPersons) {
            int age = calculateAge(person.getFirstName(), person.getLastName());

            Medicalrecord medicalRecord = dataRepository.getMedicalrecords().stream()
                    .filter(m -> m.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                            m.getLastName().equalsIgnoreCase(person.getLastName()))
                    .findFirst()
                    .orElse(null);

            List<String> medications = (medicalRecord != null) ? medicalRecord.getMedications() : new ArrayList<>();
            List<String> allergies = (medicalRecord != null) ? medicalRecord.getAllergies() : new ArrayList<>();

            result.add(new PersonInfoLastNameDTO(
                    person.getLastName(),
                    person.getAddress(),
                    age,
                    person.getEmail(),
                    medications,
                    allergies));
        }

        return result;
    }

    private int calculateAge(String firstName, String lastName) {
        Medicalrecord record = dataRepository.getMedicalrecords().stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(firstName) &&
                        m.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);

        if (record != null && record.getBirthDate() != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate birthDate = LocalDate.parse(record.getBirthDate(), formatter);
                return Period.between(birthDate, LocalDate.now()).getYears();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }
}

