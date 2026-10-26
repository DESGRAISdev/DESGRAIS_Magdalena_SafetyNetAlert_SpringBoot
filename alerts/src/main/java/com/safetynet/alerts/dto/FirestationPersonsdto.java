package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirestationPersonsdto {

    private List<PersonInfoDTO> persons;
    private long adultCount;
    private long childCount;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonInfoDTO {
        private String firstName;
        private String lastName;
        private String address;
        private String phone;
    }
}