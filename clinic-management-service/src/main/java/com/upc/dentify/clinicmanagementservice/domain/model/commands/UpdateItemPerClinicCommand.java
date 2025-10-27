package com.upc.dentify.clinicmanagementservice.domain.model.commands;

public record UpdateItemPerClinicCommand(Long id,
                                        Long availableStock,
                                         Long minimumStock,
                                         Double price) {

    public UpdateItemPerClinicCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id cannot be null or less than one");
        }
        if (availableStock == null || availableStock < 0) {
            throw new IllegalArgumentException("Available stock cannot be null or less than zero");
        }
        if (minimumStock == null || minimumStock <= 0) {
            throw new IllegalArgumentException("Minimum stock cannot be null or less than one");
        }
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("Price cannot be null or less than one");
        }
    }
}
