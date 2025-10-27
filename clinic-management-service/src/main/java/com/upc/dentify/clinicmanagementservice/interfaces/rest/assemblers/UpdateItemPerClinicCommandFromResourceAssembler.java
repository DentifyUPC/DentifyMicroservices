package com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers;

import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateItemPerClinicCommand;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.UpdateItemPerClinicResource;

public class UpdateItemPerClinicCommandFromResourceAssembler {

    public static UpdateItemPerClinicCommand toCommandFromResource(UpdateItemPerClinicResource resource, Long id) {
        return new UpdateItemPerClinicCommand(id, resource.availableStock(), resource.minimumStock(),
                resource.price());
    }

}
