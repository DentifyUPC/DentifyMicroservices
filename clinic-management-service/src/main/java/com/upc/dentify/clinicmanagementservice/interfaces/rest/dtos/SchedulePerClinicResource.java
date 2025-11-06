package com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos;

import java.time.LocalTime;

public record SchedulePerClinicResource(Long id, LocalTime startTime, LocalTime endTime, Long clinicId) {
}
