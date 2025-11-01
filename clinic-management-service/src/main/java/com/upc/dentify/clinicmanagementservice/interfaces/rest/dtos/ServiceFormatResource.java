package com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos;

public record ServiceFormatResource(Long id, String name,
                                    Double totalPricePerItems,
                                    Double totalLaborPrice,
                                    Double totalServicePrice) {
}
