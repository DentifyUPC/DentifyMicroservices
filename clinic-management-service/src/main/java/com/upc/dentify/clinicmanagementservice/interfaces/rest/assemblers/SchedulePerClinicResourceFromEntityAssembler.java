package com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.SchedulePerClinic;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.SchedulePerClinicResource;

public class SchedulePerClinicResourceFromEntityAssembler {

    public static SchedulePerClinicResource toResourceFromEntity(SchedulePerClinic entity) {
        return new SchedulePerClinicResource(entity.getId(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getClinic().getId());
    }

}
