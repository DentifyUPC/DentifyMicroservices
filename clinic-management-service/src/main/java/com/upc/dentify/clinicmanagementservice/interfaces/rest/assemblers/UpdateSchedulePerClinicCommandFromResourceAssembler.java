package com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers;

import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateSchedulePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.UpdateSchedulePerClinicResource;

public class UpdateSchedulePerClinicCommandFromResourceAssembler {

    public static UpdateSchedulePerClinicCommand toCommandFromResource(Long clinicId, UpdateSchedulePerClinicResource resource) {
        return new UpdateSchedulePerClinicCommand(resource.startTime(), resource.endTime(), clinicId);
    }

}
