package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.AllergiasMedications;
import com.upc.dentify.patientattentionservice.domain.model.commands.CreateAllergiasMedicationsCommand;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateAllergiasMedicationsCommand;

import java.util.Optional;

public interface AllergiasMedicationsCommandService {
    AllergiasMedications handle(CreateAllergiasMedicationsCommand command);
    Optional<AllergiasMedications> handle(UpdateAllergiasMedicationsCommand command);
}
