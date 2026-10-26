package com.safetynet.alerts.repository;


import com.safetynet.alerts.model.Datawrapper;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.model.Person;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Datarepository {

    private List<Person> persons= new ArrayList<>();
    private List<Firestation> firestations= new ArrayList<>();
    private List<Medicalrecord> medicalrecords= new ArrayList<>();

    @PostConstruct

    public void initData () {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream inputStream = new ClassPathResource("data.json").getInputStream();
            Datawrapper data = mapper.readValue(inputStream, Datawrapper.class);
            if (data != null) {
                this.persons = data.getPersons();
                this.firestations = data.getFirestations();
                this.medicalrecords = data.getMedicalrecords();
            }
        }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    public List<Person> getPersons() {
        return persons;
    }

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public List<Medicalrecord> getMedicalrecords() {
        return medicalrecords;
    }
    // Dans DataRepository.java

    public Person savePerson(Person person) {
        this.persons.add(person);
        return person;
    }

    public boolean deletePerson(String firstName, String lastName) {
        return this.persons.removeIf(p ->
                p.getFirstName().equalsIgnoreCase(firstName) &&
                        p.getLastName().equalsIgnoreCase(lastName)
        );
    }

    public Person updatePerson(Person personToUpdate) {
        for (Person p : this.persons) {
            if (p.getFirstName().equalsIgnoreCase(personToUpdate.getFirstName())
                    && p.getLastName().equalsIgnoreCase(personToUpdate.getLastName())) {

                p.setAddress(personToUpdate.getAddress());
                p.setCity(personToUpdate.getCity());
                p.setZip(personToUpdate.getZip());
                p.setPhone(personToUpdate.getPhone());
                p.setEmail(personToUpdate.getEmail());
                return p;
            }
        }
        return null;
    }
    public Firestation saveFirestation (Firestation firestation){
         this.firestations.add(firestation);
                return firestation;
    }

    public boolean deleteFirestation(String address, String station) {
        return this.firestations.removeIf(f -> {
            boolean matchAddress = (address != null) && f.getAddress().equalsIgnoreCase(address);
            boolean matchStation = (station != null) && f.getStation().equalsIgnoreCase(station);

            if (address != null && station != null) {
                return matchAddress && matchStation;
            }
            return matchAddress || matchStation;});

    }
    public Firestation updateFirestation(Firestation firestationToUpdate) {
        for (Firestation f : this.firestations) {
            if (f.getAddress().equalsIgnoreCase(firestationToUpdate.getAddress())) {
                f.setStation(firestationToUpdate.getStation());
                return f;
            }
        }
        return null;
    }
    public Medicalrecord saveMedicalrecord(Medicalrecord medicalrecord) {
        this.medicalrecords.add(medicalrecord);
        return medicalrecord;
    }

    public boolean deleteMedicalrecord(String firstName, String lastName) {
        return this.medicalrecords.removeIf(m ->
                m.getFirstName().equalsIgnoreCase(firstName) &&
                        m.getLastName().equalsIgnoreCase(lastName)
        );
    }

    public Medicalrecord updateMedicalrecord(Medicalrecord medicalrecordToUpdate) {
        for (Medicalrecord m : this.medicalrecords) {
            if (m.getFirstName().equalsIgnoreCase(medicalrecordToUpdate.getFirstName()) &&
                    m.getLastName().equalsIgnoreCase(medicalrecordToUpdate.getLastName())) {

                m.setBirthDate(medicalrecordToUpdate.getBirthDate());
                m.setMedications(medicalrecordToUpdate.getMedications());
                m.setAllergies(medicalrecordToUpdate.getAllergies());
                return m;
            }
        }
        return null;
    }

        }




