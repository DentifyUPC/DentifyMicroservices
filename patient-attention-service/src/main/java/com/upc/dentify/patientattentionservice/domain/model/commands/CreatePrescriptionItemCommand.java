package com.upc.dentify.patientattentionservice.domain.model.commands;

public record CreatePrescriptionItemCommand(String medicationName, String description,
                                            Long prescriptionId) {
    public CreatePrescriptionItemCommand {
        if (medicationName == null || medicationName.isBlank()) {
            throw new IllegalArgumentException("Medication name cannot be blank");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be blank");
        }

        if (prescriptionId == null || prescriptionId <= 0L) {
            throw new IllegalArgumentException("Prescription id cannot be under zero");
        }
    }
}
