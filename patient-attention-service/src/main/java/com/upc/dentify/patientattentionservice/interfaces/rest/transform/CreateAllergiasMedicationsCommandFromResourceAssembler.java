package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.commands.CreateAllergiasMedicationsCommand;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.CreateAllergiasMedicationsResource;

public class CreateAllergiasMedicationsCommandFromResourceAssembler {
    public static CreateAllergiasMedicationsCommand toCommandFromResource(CreateAllergiasMedicationsResource resource) {
        return new CreateAllergiasMedicationsCommand(
                resource.medicationName(),
                resource.clinicalRecordId()
        );
    }
}
