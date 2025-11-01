package com.upc.dentify.clinicmanagementservice.application.internal.queryservices;

import com.upc.dentify.clinicmanagementservice.application.internal.outboundservices.acl.ExternalServicesService;
import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ServicesPerClinics;
import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetAllServicesPerClinicsQuery;
import com.upc.dentify.clinicmanagementservice.domain.services.ServicePerClinicQueryService;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ClinicRepository;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ServicePerClinicRepository;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ServiceResource;
import jakarta.ws.rs.core.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePerClinicQueryServiceImpl implements ServicePerClinicQueryService {
    private final ServicePerClinicRepository servicePerClinicRepository;
    private final ExternalServicesService externalServicesService;
    private final ClinicRepository clinicRepository;

    public ServicePerClinicQueryServiceImpl(ServicePerClinicRepository servicePerClinicRepository,
                                            ExternalServicesService externalServicesService, ClinicRepository clinicRepository) {
        this.servicePerClinicRepository = servicePerClinicRepository;
        this.externalServicesService = externalServicesService;
        this.clinicRepository = clinicRepository;
    }

    @Override
    public boolean existsByClinicIdAndServiceId(Long clinicId, Long serviceId) {
        return servicePerClinicRepository.existsByClinicIdAndServiceId(clinicId, serviceId);
    }

    @Override
    public List<ServiceResource> handle(GetAllServicesPerClinicsQuery query) {
        if(!clinicRepository.existsById(query.clinicId())) {
            throw new IllegalArgumentException("Clinic with id " + query.clinicId() + " doesn't exists");
        }

        List<Long> serviceIds = servicePerClinicRepository.findByClinicId(query.clinicId())
                .stream()
                .map(ServicesPerClinics::getServiceId)
                .toList();

        List<ServiceResource> services = externalServicesService.getAllServices();

        return services.stream()
                .filter(service -> serviceIds.contains(service.id()))
                .toList();
    }
}
