package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateAnamnesisCommand;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.UpdateAnamnesisResource;

public class UpdateAnamnesisCommandFromResourceAssembler {
    public static UpdateAnamnesisCommand toCommandFromResource(Long id, UpdateAnamnesisResource resource) {
        return new UpdateAnamnesisCommand(
                id,
                resource.clinicalBackground(),
                resource.lowBloodPressure(),
                resource.highBloodPressure(),
                resource.smoke(),
                resource.currentMedications(),
                resource.emergencyContact()
        );
    }
}
