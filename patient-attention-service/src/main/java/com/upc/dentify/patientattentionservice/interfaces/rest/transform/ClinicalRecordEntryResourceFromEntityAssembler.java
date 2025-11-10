package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.ClinicalRecordEntries;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.ClinicalRecordEntryResource;

public class ClinicalRecordEntryResourceFromEntityAssembler {
    public static ClinicalRecordEntryResource fromEntityToResource(ClinicalRecordEntries entity) {
        return new ClinicalRecordEntryResource(
                entity.getId(),
                entity.getEvolution() != null ? entity.getEvolution() : null,
                entity.getObservation() != null ? entity.getObservation() : null,
                entity.getOdontologistId(),
                entity.getClinicalRecords().getId(),
                entity.getAppointmentId(),
                entity.getPrescription().getId()
        );
    }
}
