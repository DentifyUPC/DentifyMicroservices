package com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos;

public record ServicePerClinicResource(
        Long id,
        Long clinicId,
        Long serviceId,
        Double totalPricePerItems,
        Double totalLaborPrice,
        Double totalServicePrice
) {
}
