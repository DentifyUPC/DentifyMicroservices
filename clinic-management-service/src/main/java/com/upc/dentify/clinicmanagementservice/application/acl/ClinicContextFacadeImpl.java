package com.upc.dentify.clinicmanagementservice.application.acl;

import com.upc.dentify.clinicmanagementservice.domain.services.ClinicQueryService;
import com.upc.dentify.clinicmanagementservice.interfaces.acl.ClinicContextFacade;
import org.springframework.stereotype.Service;

@Service
public class ClinicContextFacadeImpl implements ClinicContextFacade {
    private final ClinicQueryService clinicQueryService;

    public ClinicContextFacadeImpl(ClinicQueryService clinicQueryService) {
        this.clinicQueryService = clinicQueryService;
    }


    @Override
    public boolean existsByClinicId(Long clinicId) {
        return clinicQueryService.existsByClinicId(clinicId);
    }
}
