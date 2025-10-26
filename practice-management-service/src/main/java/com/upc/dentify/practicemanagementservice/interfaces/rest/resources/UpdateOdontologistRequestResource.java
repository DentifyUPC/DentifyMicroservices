package com.upc.dentify.practicemanagementservice.interfaces.rest.resources;

public record UpdateOdontologistRequestResource(
        String gender,
        String street,
        String district,
        String department,
        String province,
        String phoneNumber,
        String professionalLicenseNumber,
        String specialtyRegistrationNumber,
        String specialty,
        Long serviceId,
        boolean isActive
) {
}
