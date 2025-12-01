package com.upc.dentify.appointmentservice.interfaces.rest.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateAppointmentResource(
        Long patientId, Long odontologistId,
        @JsonFormat(pattern = "HH:mm") LocalTime startTime,
        @JsonFormat(pattern = "HH:mm") LocalTime endTime,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate appointmentDate, String shiftName, Long clinicId, Long serviceId
) {
}
