package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Patient;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.PatientExternalResource;

public class PatientExternalResourceFromEntityAssembler {
    public static PatientExternalResource fromEntity(Patient patient) {
        return new PatientExternalResource(
                patient.getId(),
                patient.getPersonName().firstName(),
                patient.getPersonName().lastName()
        );
    }
}
