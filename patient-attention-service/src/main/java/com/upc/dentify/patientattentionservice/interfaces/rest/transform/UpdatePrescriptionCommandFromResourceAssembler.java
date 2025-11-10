package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.commands.UpdatePrescriptionCommand;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.UpdatePrescriptionResource;

public class UpdatePrescriptionCommandFromResourceAssembler {
    public static UpdatePrescriptionCommand fromResourceToCommand(Long id, UpdatePrescriptionResource resource) {
        return new UpdatePrescriptionCommand(id, resource.effects());
    }
}
