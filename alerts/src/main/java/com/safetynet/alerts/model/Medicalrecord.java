package com.safetynet.alerts.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@ Data
public class Medicalrecord {

    private String firstName;
    private String lastName ;
    @JsonProperty("birthdate")
    private String birthDate;
    private List< String > medications;
    private List<String> allergies ;

}
