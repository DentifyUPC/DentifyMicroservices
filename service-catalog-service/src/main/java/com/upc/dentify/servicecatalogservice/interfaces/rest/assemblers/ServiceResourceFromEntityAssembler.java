package com.upc.dentify.servicecatalogservice.interfaces.rest.assemblers;

import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Service;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ServiceResource;

public class ServiceResourceFromEntityAssembler {

    public static ServiceResource toResourceFromEntity(Service entity) {
        return new ServiceResource(entity.getId(), entity.getName());
    }
}
