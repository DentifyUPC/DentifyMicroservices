package com.upc.dentify.clinicmanagementservice.domain.model.commands;

public record CreateServicePerClinicCommand(
        Long clinicId,
        Long serviceId,
        Double totalLaborPrice
) {
}
