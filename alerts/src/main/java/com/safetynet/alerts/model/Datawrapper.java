package com.safetynet.alerts.model;

import lombok.Data;
import java.util.List;

@Data
public class Datawrapper {

    private List<Person> persons;
    private List<Firestation> firestations;
    private List<Medicalrecord> medicalrecords;
}
