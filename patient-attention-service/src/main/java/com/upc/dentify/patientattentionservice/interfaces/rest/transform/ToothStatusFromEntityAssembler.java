package com.upc.dentify.patientattentionservice.interfaces.rest.transform;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.ToothStatus;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.ToothStatusResource;

public class ToothStatusFromEntityAssembler {

    public static ToothStatusResource toResourceFromEntity(ToothStatus entity) {
        return new ToothStatusResource(
                entity.getId(),
                entity.getName()
        );
    }

}
