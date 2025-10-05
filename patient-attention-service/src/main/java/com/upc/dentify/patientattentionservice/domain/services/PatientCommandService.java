package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.Patient;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdatePatientCommand;
import com.upc.dentify.patientattentionservice.domain.model.events.UserCreatedEvent;

import java.util.Optional;

public interface PatientCommandService {
    Optional<Patient> handle(UpdatePatientCommand command);
    void handle(UserCreatedEvent event);
}
