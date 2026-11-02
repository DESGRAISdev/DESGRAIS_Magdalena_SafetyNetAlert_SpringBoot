package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Medicalrecord;
import com.safetynet.alerts.repository.Datarepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalrecordService {

    private final Datarepository dataRepository;

    @Autowired
    public MedicalrecordService(Datarepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Medicalrecord addMedicalrecord(Medicalrecord medicalrecord) {
        return dataRepository.saveMedicalrecord(medicalrecord);
    }

    public Medicalrecord updateMedicalrecord(Medicalrecord medicalrecord) {
        List<Medicalrecord> records = dataRepository.getMedicalrecords();
        for (Medicalrecord mr : records) {
            if (mr.getFirstName().equalsIgnoreCase(medicalrecord.getFirstName()) &&
                    mr.getLastName().equalsIgnoreCase(medicalrecord.getLastName())) {

                mr.setBirthDate(medicalrecord.getBirthDate());
                mr.setMedications(medicalrecord.getMedications());
                mr.setAllergies(medicalrecord.getAllergies());
                return mr;
            }
        }
        return null; // Non trouvé
    }

    public boolean deleteMedicalrecord(String firstName, String lastName) {
        return dataRepository.deleteMedicalrecord(firstName, lastName);

    }
}
