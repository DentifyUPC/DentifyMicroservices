package com.upc.dentify.patientattentionservice.interfaces.rest.resources;

public record UpdateAnamnesisResource(
        String clinicalBackground, Boolean lowBloodPressure,
        Boolean highBloodPressure, Boolean smoke, String currentMedications, String emergencyContact
) {
}
