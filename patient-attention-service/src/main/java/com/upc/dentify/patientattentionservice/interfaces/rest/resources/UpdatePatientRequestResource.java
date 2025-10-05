package com.upc.dentify.patientattentionservice.interfaces.rest.resources;

public record UpdatePatientRequestResource(
        String gender,
        String street,
        String district,
        String department,
        String province,
        String phoneNumber
) {
}
