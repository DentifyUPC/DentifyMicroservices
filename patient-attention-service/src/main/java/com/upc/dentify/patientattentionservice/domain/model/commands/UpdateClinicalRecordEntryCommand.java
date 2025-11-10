package com.upc.dentify.patientattentionservice.domain.model.commands;

public record UpdateClinicalRecordEntryCommand(Long id,
                                               String evolution,
                                               String observation) {
}
