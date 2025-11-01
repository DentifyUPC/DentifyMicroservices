package com.upc.dentify.clinicmanagementservice.application.internal.queryservices;

import com.upc.dentify.clinicmanagementservice.application.internal.outboundservices.acl.ExternalServicesService;
import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ServicesPerClinics;
import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetAllServicesPerClinicsQuery;
import com.upc.dentify.clinicmanagementservice.domain.services.ServicePerClinicQueryService;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ClinicRepository;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ServicePerClinicRepository;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ServiceFormatResource;
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
    public List<ServiceFormatResource> handle(GetAllServicesPerClinicsQuery query) {
        if(!clinicRepository.existsById(query.clinicId())) {
            throw new IllegalArgumentException("Clinic with id " + query.clinicId() + " doesn't exists");
        }

        List<ServicesPerClinics> servicesPerClinics = servicePerClinicRepository.findByClinicId(query.clinicId());

        List<ServiceResource> services = externalServicesService.getAllServices();

        return servicesPerClinics.stream()
                .flatMap(localService -> services.stream()
                        .filter(external -> external.id().equals(localService.getServiceId()))
                        .map(external -> new ServiceFormatResource(
                                external.id(),
                                external.name(),
                                localService.getTotalPricePerItems(),
                                localService.getTotalLaborPrice(),
                                localService.getTotalServicePrice()
                        ))
                ).toList();
    }
}
