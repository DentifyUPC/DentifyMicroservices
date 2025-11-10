package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.PrescriptionItems;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.PrescriptionItemResource;

public class PrescriptionItemResourceFromEntityAssembler {
    public static PrescriptionItemResource fromEntityToResource(PrescriptionItems entity) {
        return new PrescriptionItemResource(
                entity.getId(),
                entity.getMedicationName(),
                entity.getDescription(),
                entity.getPrescription().getId()
        );
    }
}
