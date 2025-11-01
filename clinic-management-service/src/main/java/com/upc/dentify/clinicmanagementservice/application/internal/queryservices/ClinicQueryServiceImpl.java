package com.upc.dentify.clinicmanagementservice.application.internal.queryservices;

import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetAllClinicsInformationPreRegisterQuery;
import com.upc.dentify.clinicmanagementservice.domain.services.ClinicQueryService;
import com.upc.dentify.clinicmanagementservice.infrastructure.persistence.jpa.repositories.ClinicRepository;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ClinicInformationPreRegister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicQueryServiceImpl implements ClinicQueryService {

    private final ClinicRepository clinicRepository;

    public ClinicQueryServiceImpl(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    @Override
    public List<ClinicInformationPreRegister> handle(GetAllClinicsInformationPreRegisterQuery query) {
        return clinicRepository.findAllClinicInformationPreRegister();
    }

    @Override
    public boolean existsByClinicId(Long clinicId) {
        return clinicRepository.existsById(clinicId);
    }
}
