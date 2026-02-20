package com.translator.mapper;

import com.translator.model.CanonicalPatient;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class FhirMapper {
    public Patient mapPatient(CanonicalPatient canonical) {
        Patient patient = new Patient();

        patient.addIdentifier()
                .setSystem("urn:system:id")
                .setValue(canonical.getIdentifier());

        patient.addName()
                .setFamily(canonical.getLastName())
                .addGiven(canonical.getFirstName());

        if (canonical.getGender() != null) {
            patient.setGender(Enumerations.AdministrativeGender.fromCode(canonical.getGender().toLowerCase()));
        }

        if (canonical.getBirthDate() != null) {
            patient.setBirthDate(Date.valueOf(canonical.getBirthDate()));
        }

        return patient;
    }
}
