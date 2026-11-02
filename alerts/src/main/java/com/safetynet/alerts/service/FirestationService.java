package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.model.Firestation;
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
import java.util.stream.Collectors;

@Service
public class FirestationService {

    private final Datarepository dataRepository;

    @Autowired
    public FirestationService(Datarepository dataRepository) {
        this.dataRepository = dataRepository;

    }

    public Firestation addFirestation(Firestation firestation) {
        return dataRepository.saveFirestation(firestation);

    }

    public Firestation updateFirestation(Firestation firestation) {
        return dataRepository.updateFirestation(firestation);
    }

    public boolean deleteFirestation(String address, String station) {
        return dataRepository.deleteFirestation(address, station);
    }

    public FirestationPersonsdto getPersonsByStationNumber(String stationNumber) {

        List<String> addresses = dataRepository.getFirestations().stream()
                .filter(f -> f.getStation().equalsIgnoreCase(stationNumber))
                .map(Firestation::getAddress)
                .toList();

        List<Person> residents = dataRepository.getPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .toList();

        List<PersonInfoDTO> personDtos = new ArrayList<>();
        int adultCount = 0;
        int childCount = 0;

        for (Person person : residents) {
            personDtos.add(new PersonInfoDTO(
                    person.getFirstName(),
                    person.getLastName(),
                    person.getAddress(),
                    person.getPhone()
            ));

            boolean isChild = checkIfChild(person.getFirstName(), person.getLastName());
            if (isChild) {
                childCount++;
            } else {
                adultCount++;
            }
        }

        return new FirestationPersonsdto(personDtos, adultCount, childCount);
    }
    boolean checkIfChild(String firstName, String lastName) {
        int age = calculateAge(firstName, lastName);
        return age > 0 && age <= 18;
    }
    public ChildAlertDTO getChildAlertByAddress(String address) {

        List<Person> residents = dataRepository.getPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .toList();

        List<ChildDetailDTO> children = new ArrayList<>();
        List<FamilyMemberDTO> otherMembers = new ArrayList<>();

        for (Person person : residents) {
            int age = calculateAge(person.getFirstName(), person.getLastName());

            if (age <= 18) {
                children.add(new ChildDetailDTO(person.getFirstName(),
                        person.getLastName(),
                        age
                ));
            } else {
                otherMembers.add(new FamilyMemberDTO(person.getFirstName(),
                        person.getLastName()
                ));
            }
        }
        if (children.isEmpty()) {
            return new ChildAlertDTO(new ArrayList<>(), new ArrayList<>());
        }

        return new ChildAlertDTO(children, otherMembers);
    }

    int calculateAge(String firstName, String lastName) {
        Medicalrecord record = dataRepository.getMedicalrecords().stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(firstName) &&
                        m.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);

        if (record != null && record.getBirthDate() != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate birthDate = LocalDate.parse(record.getBirthDate(), formatter);
                int age = Period.between(birthDate, LocalDate.now()).getYears();
                return age;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }
    public List<String> getPhoneAlertByFirestationNumber(String stationNumber) {
        List<String> addresses = dataRepository.getFirestations().stream()
                .filter(f -> f.getStation().equals(stationNumber))
                .map(Firestation::getAddress)
                .toList();
        if (addresses.isEmpty()) {
            return new ArrayList<>();
        }
        return dataRepository.getPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .map(Person::getPhone)
                .distinct()
                .toList();
    }

    public FireDTO getPersonsAndStationByAddress(String address) {
        Firestation firestation = dataRepository.getFirestations().stream()
                .filter(f -> f.getAddress().equals(address))
                .findFirst()
                .orElse(null);

        String stationNumber = (firestation != null) ? firestation.getStation() : "";

        List<Person> personsAtAddress = dataRepository.getPersons().stream()
                .filter(p -> p.getAddress().equals(address))
                .collect(Collectors.toList());

        List<FirePersonDTO> firePersons = new ArrayList<>();
        for (Person person : personsAtAddress) {
            int age = calculateAge(person.getFirstName(), person.getLastName());

            Medicalrecord medicalRecord = dataRepository.getMedicalrecords().stream()
                    .filter(m -> m.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                            m.getLastName().equalsIgnoreCase(person.getLastName()))
                    .findFirst()
                    .orElse(null);

            List<String> medications = (medicalRecord != null) ? medicalRecord.getMedications() : new ArrayList<>();
            List<String> allergies = (medicalRecord != null) ? medicalRecord.getAllergies() : new ArrayList<>();

            firePersons.add(new FirePersonDTO(
                    person.getFirstName(),
                    person.getLastName(),
                    person.getPhone(),
                    age,
                    medications,
                    allergies));
        }

        return new FireDTO(stationNumber, firePersons);
    }
    }





