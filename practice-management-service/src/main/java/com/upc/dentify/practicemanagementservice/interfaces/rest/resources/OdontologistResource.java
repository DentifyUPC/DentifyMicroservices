package com.upc.dentify.practicemanagementservice.interfaces.rest.resources;

public record OdontologistResource(
        Long id,
        String firstName,
        String lastName,
        String birthDate,
        String email,
        String gender,
        String street,
        String district,
        String province,
        String department,
        String phoneNumber,
        Integer age,
        Long clinicId,
        String professionalLicenseNumber,
        String specialtyRegistrationNumber,
        String specialty,
        Long userId,
        Long serviceId,
        boolean isActive
) {
}
