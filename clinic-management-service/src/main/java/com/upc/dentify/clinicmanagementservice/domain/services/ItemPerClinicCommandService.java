package com.upc.dentify.clinicmanagementservice.domain.services;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.ItemPerClinic;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateItemPerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateItemPerClinicCommand;

import java.util.Optional;

public interface ItemPerClinicCommandService {
    Optional<ItemPerClinic> handle(CreateItemPerClinicCommand command);
    Optional<ItemPerClinic> handle(UpdateItemPerClinicCommand command);
}
