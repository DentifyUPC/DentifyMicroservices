package com.upc.dentify.patientattentionservice.domain.model.commands;

public record UpdateAnamnesisCommand(
        Long id, String clinicalBackground, Boolean lowBloodPressure, Boolean highBloodPressure, Boolean smoke,
        String currentMedications, String emergencyContact
) {
    public UpdateAnamnesisCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }

        if (clinicalBackground == null || clinicalBackground.isEmpty()) {
            throw new IllegalArgumentException("Clinical background cannot be empty");
        }

        if (currentMedications == null || currentMedications.isEmpty()) {
            throw new IllegalArgumentException("Current medications cannot be empty");
        }

        if (emergencyContact == null || emergencyContact.isEmpty()) {
            throw new IllegalArgumentException("Emergency contact cannot be empty");
        }
    }
}
