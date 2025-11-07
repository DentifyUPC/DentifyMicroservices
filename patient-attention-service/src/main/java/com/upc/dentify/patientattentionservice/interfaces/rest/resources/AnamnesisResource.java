package com.upc.dentify.patientattentionservice.interfaces.rest.resources;

public record AnamnesisResource(
        Long id, String clinicalBackground, Boolean lowBloodPressure,
        Boolean highBloodPressure, Boolean smoke, String currentMedications, String emergencyContact
) {
}
