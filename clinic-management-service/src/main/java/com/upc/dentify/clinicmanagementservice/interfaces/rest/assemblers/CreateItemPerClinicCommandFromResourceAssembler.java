package com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers;

import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateItemPerClinicCommand;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.CreateItemPerClinicResource;

public class CreateItemPerClinicCommandFromResourceAssembler {

    public static CreateItemPerClinicCommand toCommandFromResource(CreateItemPerClinicResource resource) {
        return new CreateItemPerClinicCommand(resource.availableStock(),
                resource.minimumStock(),
                resource.price(),
                resource.itemId(),
                resource.clinicId());
    }
}
