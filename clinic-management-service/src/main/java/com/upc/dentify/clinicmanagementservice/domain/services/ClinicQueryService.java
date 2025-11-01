package com.upc.dentify.clinicmanagementservice.domain.services;

import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetAllClinicsInformationPreRegisterQuery;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.ClinicInformationPreRegister;

import java.util.List;

public interface ClinicQueryService {
    List<ClinicInformationPreRegister> handle(GetAllClinicsInformationPreRegisterQuery query);
    boolean existsByClinicId(Long clinicId);
}
