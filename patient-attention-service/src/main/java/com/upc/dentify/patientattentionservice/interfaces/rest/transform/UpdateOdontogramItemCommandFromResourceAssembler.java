package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateOdontogramItemCommand;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.UpdateOdontogramItemResource;

public class UpdateOdontogramItemCommandFromResourceAssembler {

    public static UpdateOdontogramItemCommand toCommandFromResource(Long id, UpdateOdontogramItemResource resource) {
        return new UpdateOdontogramItemCommand(
                id,
                resource.toothStatusId()
        );
    }

}
