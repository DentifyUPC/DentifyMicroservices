package com.upc.dentify.appointmentservice.interfaces.rest.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentByOdontologistResource(
        Long id,
        String state,
        Long patientId,
        String patientFirstName,
        String patientLastName,
        String shiftName,
        @JsonFormat(pattern = "HH:mm") LocalTime startTime,
        @JsonFormat(pattern = "HH:mm") LocalTime endTime,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate appointmentDate,
        String weekday,
        Long clinicId
) {
}
