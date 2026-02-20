package com.translator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CanonicalPatient {
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate birthDate;
    private String identifier;
}
