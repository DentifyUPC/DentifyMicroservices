package com.upc.dentify.clinicmanagementservice.domain.services;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ServicesPerClinics;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateServicePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateServicePerClinicCommand;

import java.util.Optional;

public interface ServicePerClinicCommandService {
    Optional<ServicesPerClinics> handle(CreateServicePerClinicCommand command);
    Optional<ServicesPerClinics> handle(UpdateServicePerClinicCommand command);
}
