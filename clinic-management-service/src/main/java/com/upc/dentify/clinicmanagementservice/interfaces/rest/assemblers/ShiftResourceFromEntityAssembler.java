package com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.Shift;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ShiftResource;

public class ShiftResourceFromEntityAssembler {

    public static ShiftResource toResourceFromEntity(Shift entity) {
        return new ShiftResource(
                entity.getId(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getName().name(),
                entity.getClinic().getId());
    }

}
