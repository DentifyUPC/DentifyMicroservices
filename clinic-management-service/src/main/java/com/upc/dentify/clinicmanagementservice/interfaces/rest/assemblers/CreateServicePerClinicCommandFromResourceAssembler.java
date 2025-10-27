package com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers;

import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateServicePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.CreateServicePerClinicResource;

public class CreateServicePerClinicCommandFromResourceAssembler {
    public static CreateServicePerClinicCommand toCommandFromResource(CreateServicePerClinicResource resource) {
        return new CreateServicePerClinicCommand(
                resource.clinicId(),
                resource.serviceId(),
                resource.totalLaborPrice()
        );
    }
}
