package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonInfoDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
}
