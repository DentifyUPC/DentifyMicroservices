package com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers;

import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateShiftCommand;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.UpdateShiftResource;

public class UpdateShiftCommandFromResourceAssembler {

    public static UpdateShiftCommand toCommandFromResource(Long id, UpdateShiftResource resource) {
        return new UpdateShiftCommand(id, resource.startTime(), resource.endTime());
    }

}
