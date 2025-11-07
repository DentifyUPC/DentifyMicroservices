package com.upc.dentify.appointmentservice.interfaces.rest.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record AppointmentByOdontologistResource(
        Long id,
        String state,
        String patientFirstName,
        String patientLastName,
        String shiftName,
        @JsonFormat(pattern = "HH:mm") LocalTime startTime,
        @JsonFormat(pattern = "HH:mm") LocalTime endTime,
        Long clinicId
) {
}
