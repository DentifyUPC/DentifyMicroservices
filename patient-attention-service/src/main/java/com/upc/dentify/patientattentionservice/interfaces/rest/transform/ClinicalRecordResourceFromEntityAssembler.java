package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.ClinicalRecords;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.ClinicalRecordResource;

public class ClinicalRecordResourceFromEntityAssembler {
    public static ClinicalRecordResource fromEntityToResource(ClinicalRecords entity) {
        return new ClinicalRecordResource(
                entity.getId(),
                entity.getPatient().getId(),
                entity.getAnamnesis().getId(),
                entity.getOdontogram().getId()
        );
    }
}
