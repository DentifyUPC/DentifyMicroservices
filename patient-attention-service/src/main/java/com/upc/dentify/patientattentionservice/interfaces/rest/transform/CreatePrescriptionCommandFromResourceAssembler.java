package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.commands.CreatePrescriptionItemCommand;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.CreatePrescriptionItemResource;

public class CreatePrescriptionCommandFromResourceAssembler {
    public static CreatePrescriptionItemCommand fromResourceToCommand(CreatePrescriptionItemResource resource) {
        return new CreatePrescriptionItemCommand(
                resource.medicationName(),
                resource.description(),
                resource.prescriptionId()
        );
    }
}
