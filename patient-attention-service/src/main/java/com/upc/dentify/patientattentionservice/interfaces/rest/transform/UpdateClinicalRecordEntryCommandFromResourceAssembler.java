package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateClinicalRecordEntryCommand;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.UpdateClinicalRecordEntryResource;

public class UpdateClinicalRecordEntryCommandFromResourceAssembler {
    public static UpdateClinicalRecordEntryCommand fromResourceToCommand(Long id, UpdateClinicalRecordEntryResource resource) {
        return new UpdateClinicalRecordEntryCommand(
                id, resource.evolution(), resource.observation()
        );
    }
}
