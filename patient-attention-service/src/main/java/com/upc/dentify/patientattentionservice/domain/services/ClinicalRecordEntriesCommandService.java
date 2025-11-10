package com.upc.dentify.patientattentionservice.domain.services;

import com.upc.dentify.patientattentionservice.domain.model.aggregates.ClinicalRecordEntries;
import com.upc.dentify.patientattentionservice.domain.model.commands.UpdateClinicalRecordEntryCommand;
import com.upc.dentify.patientattentionservice.domain.model.events.AppointmentCreatedEvent;

import java.util.Optional;

public interface ClinicalRecordEntriesCommandService {
    void handle(AppointmentCreatedEvent event);
    Optional<ClinicalRecordEntries> handle(UpdateClinicalRecordEntryCommand command);
}
