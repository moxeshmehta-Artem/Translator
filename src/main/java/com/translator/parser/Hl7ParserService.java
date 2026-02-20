package com.translator.parser;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import com.translator.model.CanonicalPatient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class Hl7ParserService {
    public CanonicalPatient parse(String payload) {
        try (HapiContext context = new DefaultHapiContext()) {
            // HL7 segment separator should be \r (carriage return)
            String normalizedPayload = payload.replace("\n", "\r").replace("\r\r", "\r");

            PipeParser parser = context.getPipeParser();
            Message hl7Msg = parser.parse(normalizedPayload);

            CanonicalPatient patient = new CanonicalPatient();

            // Extract using Terser for better compatibility across versions
            ca.uhn.hl7v2.util.Terser terser = new ca.uhn.hl7v2.util.Terser(hl7Msg);

            patient.setFirstName(terser.get("/.PID-5-2"));
            patient.setLastName(terser.get("/.PID-5-1"));
            patient.setIdentifier(terser.get("/.PID-3-1"));

            String gender = terser.get("/.PID-8");
            if ("M".equalsIgnoreCase(gender))
                patient.setGender("male");
            else if ("F".equalsIgnoreCase(gender))
                patient.setGender("female");

            String dob = terser.get("/.PID-7-1");
            if (dob != null && dob.length() >= 8) {
                try {
                    patient.setBirthDate(LocalDate.parse(dob.substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd")));
                } catch (Exception e) {
                    System.err.println("Failed to parse DOB: " + dob);
                }
            }

            return patient;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse HL7 patient: " + e.getMessage(), e);
        }
    }
}
