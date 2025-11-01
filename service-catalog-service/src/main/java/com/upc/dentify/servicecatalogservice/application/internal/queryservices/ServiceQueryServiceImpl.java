package com.upc.dentify.servicecatalogservice.application.internal.queryservices;

import com.upc.dentify.servicecatalogservice.domain.model.queries.GetAllServicesQuery;
import com.upc.dentify.servicecatalogservice.domain.services.ServiceQueryService;
import com.upc.dentify.servicecatalogservice.infrastructure.persistence.jpa.repositories.ServiceRepository;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ServiceResource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServiceQueryServiceImpl implements ServiceQueryService {

    private final ServiceRepository serviceRepository;

    public ServiceQueryServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<com.upc.dentify.servicecatalogservice.domain.model.aggregates.Service> handle(GetAllServicesQuery query) {
        return serviceRepository.findAll();
    }

    @Override
    public List<ServiceResource> handleDto(GetAllServicesQuery query) {
        return serviceRepository.findAllServicesResources();
    }

    @Override
    public boolean existsById(Long serviceId) {
        return serviceRepository.existsById(serviceId);
    }
}
