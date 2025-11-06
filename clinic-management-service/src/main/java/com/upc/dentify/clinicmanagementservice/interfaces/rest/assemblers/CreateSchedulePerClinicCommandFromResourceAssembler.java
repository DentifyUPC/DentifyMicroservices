package com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers;

import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateSchedulePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.CreateSchedulePerClinicResource;

public class CreateSchedulePerClinicCommandFromResourceAssembler {

    public static CreateSchedulePerClinicCommand toCommandFromResource(CreateSchedulePerClinicResource resource) {
        return new CreateSchedulePerClinicCommand(resource.startTime(), resource.endTime(), resource.clinicId());
    }
}
