package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Anamnesis;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.AnamnesisResource;

public class AnamnesisResourceFromEntityAssembler {
    public static AnamnesisResource toResourceFromEntity(Anamnesis entity) {
        return new AnamnesisResource(
                entity.getId(),
                entity.getClinicalBackground() != null ? entity.getClinicalBackground() : null,
                entity.getLowBloodPressure(),
                entity.getHighBloodPressure(),
                entity.getSmoke(),
                entity.getCurrentMedications() != null ? entity.getCurrentMedications() : null,
                entity.getEmergencyContact() != null ? entity.getEmergencyContact().phoneNumber() : null
        );
    }
}
