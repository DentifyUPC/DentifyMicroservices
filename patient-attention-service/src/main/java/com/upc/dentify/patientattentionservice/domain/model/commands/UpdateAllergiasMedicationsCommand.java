package com.upc.dentify.patientattentionservice.domain.model.commands;

public record UpdateAllergiasMedicationsCommand(Long id, String medicationName) {
    public UpdateAllergiasMedicationsCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }

        if (medicationName == null || medicationName.isEmpty()) {
            throw new IllegalArgumentException("Medication name cannot be empty");
        }
    }
}
