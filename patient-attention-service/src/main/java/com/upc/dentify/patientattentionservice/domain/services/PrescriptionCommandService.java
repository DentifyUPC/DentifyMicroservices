package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Prescription;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdatePrescriptionCommand;

import java.util.Optional;

public interface PrescriptionCommandService {
    Optional<Prescription> handle(UpdatePrescriptionCommand command);
}
