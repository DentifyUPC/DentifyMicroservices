package com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers;

import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateServicePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.UpdateServicePerClinicResource;

public class UpdateServicePerClinicCommandFromResourceAssembler {
    public static UpdateServicePerClinicCommand toCommandFromResource(Long id, UpdateServicePerClinicResource resource) {
        return new UpdateServicePerClinicCommand(id, resource.totalLaborPrice());
    }
}
