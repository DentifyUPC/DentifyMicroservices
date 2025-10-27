package com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ServicesPerClinics;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ServicePerClinicResource;

public class ServicePerClinicResourceFromEntityAssembler {
    public static ServicePerClinicResource toResourceFromEntity(ServicesPerClinics entity) {
        return new ServicePerClinicResource(
                entity.getId(),
                entity.getClinicId(),
                entity.getServiceId(),
                entity.getTotalPricePerItems(),
                entity.getTotalLaborPrice(),
                entity.getTotalServicePrice()
        );
    }
}
