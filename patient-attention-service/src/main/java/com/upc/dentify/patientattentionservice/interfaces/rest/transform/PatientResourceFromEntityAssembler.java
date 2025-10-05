package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Patient;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.PatientResource;

public class PatientResourceFromEntityAssembler {
    public static PatientResource fromEntityToResource(Patient patient) {
        return new PatientResource(
                patient.getId(),
                patient.getPersonName().firstName(),
                patient.getPersonName().lastName(),
                patient.getEmail().email(),
                patient.getBirthDate().birthDate(),
                patient.getAge(),
                patient.getGender() != null ? patient.getGender().name() : null,
                patient.getAddress() != null ? patient.getAddress().street() : null,
                patient.getAddress() != null ? patient.getAddress().district() : null,
                patient.getAddress() != null ? patient.getAddress().province() : null,
                patient.getAddress() != null ? patient.getAddress().department() : null,
                patient.getPhoneNumber() != null ? patient.getPhoneNumber().phoneNumber() : null,
                patient.getUserId()
        );
    }
}
