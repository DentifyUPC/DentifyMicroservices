package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.OdontogramItem;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.OdontogramItemResource;

public class OdontogramItemResourceFromEntityAssembler {

    public static OdontogramItemResource toResourceFromEntity(OdontogramItem entity) {
        return new OdontogramItemResource(
                entity.getId(),
                entity.getTeeth().getCode(),
                entity.getToothStatus().getName(),
                entity.getOdontogram().getId()
        );
    }

}
