package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildDetailDTO;
import com.safetynet.alerts.dto.FamilyMemberDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.Datarepository;
import com.safetynet.alerts.dto.ChildAlertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.safetynet.alerts.dto.FirestationPersonsdto;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

        List<FirestationPersonsdto.PersonInfoDTO> personDtos = new ArrayList<>();
        int adultCount = 0;
        int childCount = 0;

        for (Person person : residents) {
            personDtos.add(new FirestationPersonsdto.PersonInfoDTO(
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
    private boolean checkIfChild(String firstName, String lastName) {
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
                int age = Period.between(birthDate, LocalDate.now()).getYears();
                return age;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }
    }



