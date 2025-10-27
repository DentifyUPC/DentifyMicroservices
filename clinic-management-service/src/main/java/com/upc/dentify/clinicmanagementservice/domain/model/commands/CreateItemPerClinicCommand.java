package com.upc.dentify.clinicmanagementservice.domain.model.commands;

public record CreateItemPerClinicCommand(Long availableStock,
                                         Long minimumStock,
                                         Double price,
                                         Long itemId,
                                         Long clinicId) {

    public CreateItemPerClinicCommand {
        if (availableStock == null || availableStock < 0) {
            throw new IllegalArgumentException("Available stock cannot be null or less than zero");
        }
        if (minimumStock == null || minimumStock <= 0) {
            throw new IllegalArgumentException("Minimum stock cannot be null or less than one");
        }
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("Price cannot be null or less than one");
        }
        if (itemId == null || itemId <= 0) {
            throw new IllegalArgumentException("ItemId cannot be null or less than one");
        }
        if (clinicId == null || clinicId <= 0) {
            throw new IllegalArgumentException("Clinic Id cannot be null or less than one");
        }
    }
}
