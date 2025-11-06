package com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos;

import java.time.LocalTime;

public record ShiftResource(Long id,
                            LocalTime startTime,
                            LocalTime endTime,
                            String name,
                            Long clinicId) {
}
