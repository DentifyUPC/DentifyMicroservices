package com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos;

public record CreateServicePerClinicResource(
        Long clinicId,
        Long serviceId,
        Double totalLaborPrice
) {
}
