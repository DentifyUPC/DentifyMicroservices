package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Prescription;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.PrescriptionResource;

public class PrescriptionResourceFromEntityAssembler {
    public static PrescriptionResource fromEntityToResource(Prescription entity) {
        return new PrescriptionResource(
                entity.getId(),
                entity.getEffects() != null ? entity.getEffects() : null
        );
    }
}
