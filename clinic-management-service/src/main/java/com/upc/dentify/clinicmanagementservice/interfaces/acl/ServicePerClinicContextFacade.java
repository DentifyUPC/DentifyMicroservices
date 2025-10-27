package com.upc.dentify.clinicmanagementservice.interfaces.acl;

public interface ServicePerClinicContextFacade {
    boolean existsByClinicIdAndServiceId(Long clinicId, Long serviceId);
}
