package com.translator.service;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.translator.entity.TranslationEntity;
import com.translator.mapper.FhirMapper;
import com.translator.model.CanonicalPatient;
import com.translator.parser.Hl7ParserService;
import com.translator.parser.JsonParserService;
import com.translator.repository.TranslationRepository;
import com.translator.util.FileTypeDetector;

import ca.uhn.fhir.context.FhirContext;

@Service
public class TranslatorService {

    @Autowired
    private Hl7ParserService hl7Parser;

    @Autowired
    private JsonParserService jsonParser;

    @Autowired
    private FhirMapper mapper;

    @Autowired
    private BundleService bundleService;

    @Autowired
    private TranslationRepository repository;

    @Autowired
    private FhirContext fhirContext;

    public String translate(String payload) {
        System.out.println("Processing payload of length: " + (payload != null ? payload.length() : 0));
        TranslationEntity entity = new TranslationEntity();

        try {
            String type = FileTypeDetector.detect(payload);
            System.out.println("Detected type: " + type);

            // Clean payload if it's quoted
            String cleanPayload = payload != null ? payload.trim() : "";
            if (cleanPayload.startsWith("\"") && cleanPayload.endsWith("\"")) {
                cleanPayload = cleanPayload.substring(1, cleanPayload.length() - 1)
                        .replace("\\n", "\n")
                        .replace("\\r", "\r")
                        .trim();
            }

            entity.setInputData(payload);
            entity.setInputType(type);

            CanonicalPatient patient;

            if ("HL7".equals(type)) {
                patient = hl7Parser.parse(cleanPayload);
            } else if ("JSON".equals(type)) {
                patient = jsonParser.parse(cleanPayload);
            } else {
                throw new IllegalArgumentException("Unsupported or unrecognized input format");
            }

            System.out.println("Parsed patient: " + patient.getFirstName() + " " + patient.getLastName());

            Patient fhirPatient = mapper.mapPatient(patient);

            Bundle bundle = bundleService.createBundle(fhirPatient);

            String output = fhirContext
                    .newJsonParser()
                    .setPrettyPrint(true)
                    .encodeResourceToString(bundle);

            entity.setFhirOutput(output);
            entity.setStatus("SUCCESS");

            repository.save(entity);

            return output;

        } catch (Exception e) {
            System.err.println("Translation failed: " + e.getMessage());
            e.printStackTrace();

            entity.setStatus("FAILED");
            repository.save(entity);

            throw new RuntimeException(e);
        }
    }
}