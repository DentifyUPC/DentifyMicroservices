package com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos;

public record UpdateItemPerClinicResource(Long availableStock,
                                          Long minimumStock,
                                          Double price) {
}
