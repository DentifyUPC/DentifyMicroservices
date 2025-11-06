package com.upc.dentify.practicemanagementservice.domain.model.valueobjects;

import java.time.LocalTime;

public record StartTime(LocalTime startTime) {

    private static final LocalTime WORK_START = LocalTime.of(8, 0);
    private static final LocalTime WORK_END = LocalTime.of(18, 0);
    private static final int MINUTES_GRANULARITY = 15; // puedes cambiar a 5 si lo prefieres

    public StartTime {
        if (startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }

        // Validar dentro del horario laboral
        if (startTime.isBefore(WORK_START) || startTime.isAfter(WORK_END)) {
            throw new IllegalArgumentException("Start time must be within working hours (08:00–18:00)");
        }

        // Validar granularidad (múltiplos de 15 minutos)
        if (startTime.getMinute() % MINUTES_GRANULARITY != 0) {
            throw new IllegalArgumentException("Start time minutes must be multiple of " + MINUTES_GRANULARITY);
        }
    }

    @Override
    public String toString() {
        return startTime.toString();
    }

}
