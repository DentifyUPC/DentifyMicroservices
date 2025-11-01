package com.upc.dentify.servicecatalogservice.application.acl;

import com.upc.dentify.servicecatalogservice.domain.model.queries.GetAllServicesQuery;
import com.upc.dentify.servicecatalogservice.domain.services.ServiceQueryService;
import com.upc.dentify.servicecatalogservice.interfaces.acl.ServiceContextFacade;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ServiceResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceContextFacadeImpl implements ServiceContextFacade {
    private final ServiceQueryService serviceQueryService;

    public ServiceContextFacadeImpl(ServiceQueryService serviceQueryService) {
        this.serviceQueryService = serviceQueryService;
    }


    @Override
    public List<ServiceResource> getAllServices() {
        var query = new GetAllServicesQuery();
        return serviceQueryService.handleDto(query);
    }
}
