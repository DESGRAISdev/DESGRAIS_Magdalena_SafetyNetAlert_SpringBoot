package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.Datarepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * Méthode utilitaire privée pour éviter de dupliquer la recherche de dossier médical
     */
    private Medicalrecord findMedicalrecord(String firstName, String lastName) {
        return dataRepository.getMedicalrecords().stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(firstName) &&
                        m.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
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
        Medicalrecord record = findMedicalrecord(firstName, lastName);
        int age = (record != null) ? record.calculateAge() : 0;
        return age > 0 && age <= 18;
    }

    public ChildAlertDTO getChildAlertByAddress(String address) {
        List<Person> residents = dataRepository.getPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .toList();

        List<ChildDetailDTO> children = new ArrayList<>();
        List<FamilyMemberDTO> otherMembers = new ArrayList<>();

        for (Person person : residents) {
            Medicalrecord record = findMedicalrecord(person.getFirstName(), person.getLastName());
            int age = (record != null) ? record.calculateAge() : 0;

            if (age <= 18) {
                children.add(new ChildDetailDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        age
                ));
            } else {
                otherMembers.add(new FamilyMemberDTO(
                        person.getFirstName(),
                        person.getLastName()
                ));
            }
        }
        if (children.isEmpty()) {
            return new ChildAlertDTO(new ArrayList<>(), new ArrayList<>());
        }

        return new ChildAlertDTO(children, otherMembers);
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
            Medicalrecord medicalRecord = findMedicalrecord(person.getFirstName(), person.getLastName());

            int age = (medicalRecord != null) ? medicalRecord.calculateAge() : 0;
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

    public List<FloodStationDTO> getFloodStations(List<String> stationNumbers) {
        List<String> addresses = dataRepository.getFirestations().stream()
                .filter(f -> stationNumbers.contains(f.getStation()))
                .map(Firestation::getAddress)
                .distinct()
                .toList();

        List<FloodStationDTO> result = new ArrayList<>();

        for (String address : addresses) {
            List<Person> residents = dataRepository.getPersons().stream()
                    .filter(p -> p.getAddress() != null && p.getAddress().equalsIgnoreCase(address))
                    .toList();
            List<FloodPersonDTO> floodPersons = new ArrayList<>();
            for (Person person : residents) {
                Medicalrecord medicalRecord = findMedicalrecord(person.getFirstName(), person.getLastName());

                int age = (medicalRecord != null) ? medicalRecord.calculateAge() : 0;
                List<String> medications = (medicalRecord != null) ? medicalRecord.getMedications() : new ArrayList<>();
                List<String> allergies = (medicalRecord != null) ? medicalRecord.getAllergies() : new ArrayList<>();

                floodPersons.add(new FloodPersonDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getPhone(),
                        age,
                        medications,
                        allergies
                ));
            }

            result.add(new FloodStationDTO(address, floodPersons));
        }

        return result;
    }
}






