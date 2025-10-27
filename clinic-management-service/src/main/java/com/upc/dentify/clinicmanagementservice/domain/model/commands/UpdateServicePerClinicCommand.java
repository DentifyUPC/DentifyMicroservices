package com.upc.dentify.clinicmanagementservice.domain.model.commands;

public record UpdateServicePerClinicCommand(Long id,
                                            Double totalLaborPrice) {
    public UpdateServicePerClinicCommand {
        if (id == null || id < 0L) {
            throw new IllegalArgumentException("Id cannot be negative");
        }
        if (totalLaborPrice == null || totalLaborPrice < 0.0) {
            throw new IllegalArgumentException("TotalLaborPrice cannot be negative");
        }
    }
}
