package com.upc.dentify.appointmentservice.domain.model.command;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateAppointmentCommand(
        Long patientId, Long odontologistId,
        LocalTime startTime, LocalTime endTime, LocalDate appointmentDate,
        String shiftName, Long clinicId
) {
    public CreateAppointmentCommand {
        if (patientId == null || patientId <= 0L) {
            throw new IllegalArgumentException("patientId must be greater than 0");
        }

        if (odontologistId == null || odontologistId <= 0L) {
            throw new IllegalArgumentException("odontologistId must be greater than 0");
        }

        if (startTime == null) {
            throw new IllegalArgumentException("startTime must not be null");
        }

        if (endTime == null) {
            throw new IllegalArgumentException("endTime must not be null");
        }

        if (appointmentDate == null) {
            throw new IllegalArgumentException("appointmentDate must not be null");
        }

        if (shiftName == null || shiftName.isBlank()) {
            throw new IllegalArgumentException("shiftName cannot be blank");
        }

        if (clinicId == null || clinicId <= 0L) {
            throw new IllegalArgumentException("clinicId must be greater than 0");
        }
    }
}
