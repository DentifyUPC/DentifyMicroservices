package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateAllergiasMedicationsCommand;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.UpdateAllergiasMedicationsResource;

public class UpdateAllergiasMedicationsCommandFromResourceAssembler {
    public static UpdateAllergiasMedicationsCommand toCommandFromResource(Long id, UpdateAllergiasMedicationsResource resource) {
        return new UpdateAllergiasMedicationsCommand(
                id,
                resource.medicationName()
        );
    }
}
