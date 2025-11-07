package com.upc.dentify.practicemanagementservice.interfaces.rest.transform;

import com.upc.dentify.practicemanagementservice.domain.model.aggregates.Odontologist;
import com.upc.dentify.practicemanagementservice.interfaces.rest.resources.OdontologistExternalResource;

public class OdontologistExternalResourceFromEntityAssembler {
    public static OdontologistExternalResource fromEntity(Odontologist entity) {
        return new OdontologistExternalResource(
                entity.getId(),
                entity.getPersonName().firstName(),
                entity.getPersonName().lastName(),
                entity.getShiftName()
        );
    }
}
