package com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers;

import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateShiftCommand;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.CreateShiftResource;

public class CreateShiftCommandFromResourceAssembler {

    public static CreateShiftCommand toCommandFromResource(CreateShiftResource resource) {
        return new CreateShiftCommand(resource.startTime(), resource.endTime(), resource.name(), resource.clinicId());
    }

}
