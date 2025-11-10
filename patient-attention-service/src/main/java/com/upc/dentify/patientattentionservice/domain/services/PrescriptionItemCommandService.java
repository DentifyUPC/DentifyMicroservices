package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.PrescriptionItems;
import com.upc.dentify.patientattentionservice.domain.model.commands.CreatePrescriptionItemCommand;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdatePrescriptionItemCommand;

import java.util.Optional;

public interface PrescriptionItemCommandService {
    Optional<PrescriptionItems> handle(CreatePrescriptionItemCommand command);
    Optional<PrescriptionItems> handle(UpdatePrescriptionItemCommand command);
}
