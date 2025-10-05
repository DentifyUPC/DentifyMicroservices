package com.upc.dentify.patientattentionservice.interfaces.rest.resources;

public record PatientResource(
        Long id,
        String firstName,
        String lastName,
        String email,
        String birthDate,
        Integer age,
        String gender,
        String street,
        String district,
        String province,
        String department,
        String phoneNumber,
        Long userId
) {
}
