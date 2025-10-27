package com.upc.dentify.clinicmanagementservice.domain.services;

public interface ServicePerClinicQueryService {
    boolean existsByClinicIdAndServiceId(Long clinicId, Long serviceId);
}
