package com.safetynet.alerts.dto;

import java.util.List;

public class FloodStationDTO {
    private String address;
    private List<FloodPersonDTO> persons;

    public FloodStationDTO(String address, List<FloodPersonDTO> persons) {
        this.address = address;
        this.persons = persons;
    }

    public String getAddress() { return address; }
    public List<FloodPersonDTO> getPersons() { return persons; }
}
