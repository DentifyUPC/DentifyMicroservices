package com.upc.dentify.patientattentionservice.domain.model.commands;

public record UpdatePrescriptionItemCommand(Long id, String medicationName, String description) {
}
