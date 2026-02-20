package com.translator.service;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Service;

@Service
public class BundleService {
    public Bundle createBundle(Patient patient) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        bundle.addEntry()
                .setResource(patient)
                .getRequest()
                .setMethod(Bundle.HTTPVerb.POST)
                .setUrl("Patient");

        return bundle;
    }
}
