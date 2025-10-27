package com.upc.dentify.clinicmanagementservice.domain.model.valueobjects;

public record Address(
        String street,
        String number,
        String district,
        String province,
        String department
) {
    public Address {
        if (street == null || street.isBlank()) {
            throw new IllegalArgumentException("Street cannot be null or blank");
        }
        if (street.length() > 100) {
            throw new IllegalArgumentException("Street cannot exceed 100 characters");
        }

        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Number cannot be null or blank");
        }
        if (number.length() > 10) {
            throw new IllegalArgumentException("Number cannot exceed 10 characters");
        }

        if (district == null || district.isBlank()) {
            throw new IllegalArgumentException("District cannot be null or blank");
        }
        if (district.length() > 50) {
            throw new IllegalArgumentException("District cannot exceed 50 characters");
        }

        if (province == null || province.isBlank()) {
            throw new IllegalArgumentException("Province cannot be null or blank");
        }
        if (province.length() > 50) {
            throw new IllegalArgumentException("Province cannot exceed 50 characters");
        }

        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException("Department cannot be null or blank");
        }
        if (department.length() > 50) {
            throw new IllegalArgumentException("Department cannot exceed 50 characters");
        }
    }
}
