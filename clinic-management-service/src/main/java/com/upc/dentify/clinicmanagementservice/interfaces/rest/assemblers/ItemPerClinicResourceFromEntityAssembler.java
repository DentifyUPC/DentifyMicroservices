package com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ItemPerClinic;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ItemPerClinicResource;

public class ItemPerClinicResourceFromEntityAssembler {

    public static ItemPerClinicResource toResourceFromEntity(ItemPerClinic entity) {
        return new ItemPerClinicResource(entity.getId(),
                entity.getAvailableStock(),
                entity.getMinimumStock(),
                entity.getPrice(),
                entity.getItemId(),
                entity.getClinic().getId());
    }

}
