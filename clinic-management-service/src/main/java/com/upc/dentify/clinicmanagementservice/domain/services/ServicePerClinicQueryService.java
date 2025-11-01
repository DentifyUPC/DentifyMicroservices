package com.upc.dentify.clinicmanagementservice.domain.services;

import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetAllServicesPerClinicsQuery;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ServiceResource;

import java.util.List;

public interface ServicePerClinicQueryService {
    boolean existsByClinicIdAndServiceId(Long clinicId, Long serviceId);
    List<ServiceResource> handle(GetAllServicesPerClinicsQuery query);
}
