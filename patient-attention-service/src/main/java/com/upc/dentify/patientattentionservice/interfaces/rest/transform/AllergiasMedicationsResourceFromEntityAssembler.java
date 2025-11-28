package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.AllergiasMedications;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.AllergiasMedicationsResource;

public class AllergiasMedicationsResourceFromEntityAssembler {
    public static AllergiasMedicationsResource toResourceFromEntity(AllergiasMedications entity) {
        return new AllergiasMedicationsResource(
                entity.getId(),
                entity.getMedicationName(),
                entity.getClinicalRecords().getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
