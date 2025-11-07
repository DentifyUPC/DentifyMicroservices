package com.upc.dentify.appointmentservice.domain.model.command;

public record UpdateAppointmentCommand(Long id, String state) {
    public UpdateAppointmentCommand {
        if (id == null || id <= 0L) {
            throw new IllegalArgumentException("Id cannot be null or negative");
        }

        if (state == null || state.isEmpty()) {
            throw new IllegalArgumentException("State cannot be null or empty");
        }
    }
}
