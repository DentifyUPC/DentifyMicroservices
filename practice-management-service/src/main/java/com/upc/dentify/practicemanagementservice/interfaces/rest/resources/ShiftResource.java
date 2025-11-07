package com.upc.dentify.practicemanagementservice.interfaces.rest.resources;

import java.time.LocalTime;

public record ShiftResource(Long id,
                            LocalTime startTime,
                            LocalTime endTime,
                            String name,
                            Long clinicId) {
}