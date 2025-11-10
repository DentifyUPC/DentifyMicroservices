package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.commands.UpdatePrescriptionItemCommand;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.UpdatePrescriptionItemResource;

public class UpdatePrescriptionItemCommandFromResourceAssembler {
    public static UpdatePrescriptionItemCommand fromResourceToCommand(Long id, UpdatePrescriptionItemResource resource) {
        return new UpdatePrescriptionItemCommand(id, resource.medicationName(), resource.description());
    }
}
