package com.upc.dentify.practicemanagementservice.domain.model.commands;

import com.upc.dentify.practicemanagementservice.domain.model.valueobjects.Gender;

public record UpdateOdontologistCommand(
        Long odontologistId,
        Gender gender,
        String street,
        String district,
        String department,
        String province,
        String phoneNumber,
        String professionalLicenseNumber,
        String specialtyRegistrationNumber,
        String specialty,
        Long serviceId,
        String shiftName,
        boolean isActive
) {
    public UpdateOdontologistCommand {
        if (odontologistId == null || odontologistId <= 0L) {
            throw new IllegalArgumentException("Odontologist id must be greater than or equal to 0");
        }

        if (gender == null) {
            throw new IllegalArgumentException("Gender cannot be null");
        }

        if (street == null || street.isEmpty()) {
            throw new IllegalArgumentException("Street cannot be empty");
        }

        if (district == null || district.isEmpty()) {
            throw new IllegalArgumentException("District cannot be empty");
        }

        if (department == null || department.isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }

        if (province == null || province.isEmpty()) {
            throw new IllegalArgumentException("Province cannot be empty");
        }

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("PhoneNumber cannot be empty");
        }

        if (professionalLicenseNumber == null || professionalLicenseNumber.isEmpty()) {
            throw new IllegalArgumentException("Professional License Number cannot be empty");
        }

        if (specialtyRegistrationNumber == null || specialtyRegistrationNumber.isEmpty()) {
            throw new IllegalArgumentException("Specialty Registration Number cannot be empty");
        }

        if (specialty == null || specialty.isEmpty()) {
            throw new IllegalArgumentException("Specialty cannot be empty");
        }

        if (serviceId == null || serviceId <= 0L) {
            throw new IllegalArgumentException("Service id must be greater than or equal to 0");
        }

        if (shiftName == null || shiftName.isEmpty()) {
            throw new IllegalArgumentException("Shift name cannot be empty");
        }
    }
}