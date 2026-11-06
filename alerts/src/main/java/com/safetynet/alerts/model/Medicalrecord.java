package com.safetynet.alerts.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class Medicalrecord {
    private String firstName;
    private String lastName;

    @JsonProperty("birthdate")
    private String birthDate;

    private List<String> medications;
    private List<String> allergies;

    public int calculateAge() {
        if (this.birthDate == null) {
            return 0;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate parsedBirthDate = LocalDate.parse(this.birthDate, formatter);
            return Period.between(parsedBirthDate, LocalDate.now()).getYears();
        } catch (Exception e) {
            return 0;
        }
    }
}
