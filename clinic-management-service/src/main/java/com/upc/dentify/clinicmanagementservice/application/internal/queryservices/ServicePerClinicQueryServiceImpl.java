package com.upc.dentify.clinicmanagementservice.application.internal.queryservices;

import com.upc.dentify.clinicmanagementservice.domain.services.ServicePerClinicQueryService;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ServicePerClinicRepository;
import org.springframework.stereotype.Service;

@Service
public class ServicePerClinicQueryServiceImpl implements ServicePerClinicQueryService {
    private final ServicePerClinicRepository servicePerClinicRepository;

    public ServicePerClinicQueryServiceImpl(ServicePerClinicRepository servicePerClinicRepository) {
        this.servicePerClinicRepository = servicePerClinicRepository;
    }

    @Override
    public boolean existsByClinicIdAndServiceId(Long clinicId, Long serviceId) {
        return servicePerClinicRepository.existsByClinicIdAndServiceId(clinicId, serviceId);
    }
}
