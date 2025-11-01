package com.upc.dentify.servicecatalogservice.domain.services;

import com.upc.dentify.servicecatalogservice.domain.model.aggregates.Service;
import com.upc.dentify.servicecatalogservice.domain.model.queries.GetAllServicesQuery;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ServiceResource;

import java.util.List;

public interface ServiceQueryService {
    List<Service> handle(GetAllServicesQuery query);
    List<ServiceResource> handleDto(GetAllServicesQuery query);
    boolean existsById(Long serviceId);
}
