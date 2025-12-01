package com.upc.dentify.clinicmanagementservice.application.acl;

import com.upc.dentify.clinicmanagementservice.domain.services.ServicePerClinicQueryService;
import com.upc.dentify.clinicmanagementservice.interfaces.acl.ServicePerClinicContextFacade;
import org.springframework.stereotype.Service;

@Service
public class ServicePerClinicContextFacadeImpl implements ServicePerClinicContextFacade {
    private final ServicePerClinicQueryService servicePerClinicQueryService;

    public ServicePerClinicContextFacadeImpl(ServicePerClinicQueryService servicePerClinicQueryService) {
        this.servicePerClinicQueryService = servicePerClinicQueryService;
    }

    @Override
    public boolean existsByClinicIdAndServiceId(Long clinicId, Long serviceId) {
        return servicePerClinicQueryService.existsByClinicIdAndServiceId(clinicId, serviceId);
    }

    @Override
    public Double getTotalServicePrice(Long clinicId, Long serviceId) {
        return servicePerClinicQueryService.getTotalServicePrice(clinicId, serviceId);
    }
}
