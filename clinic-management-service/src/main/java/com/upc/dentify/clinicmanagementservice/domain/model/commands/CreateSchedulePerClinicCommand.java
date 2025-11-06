package com.upc.dentify.clinicmanagementservice.domain.model.commands;


import java.time.LocalTime;

public record CreateSchedulePerClinicCommand(LocalTime startTime, LocalTime endTime, Long clinicId) {

    public CreateSchedulePerClinicCommand {

        if(clinicId == null || clinicId <= 0) {
            throw new IllegalArgumentException("clinicId must be greater than 0");
        }

        if (startTime == null) {
            throw new IllegalArgumentException("startTime must not be null");
        }

        if (endTime == null) {
            throw new IllegalArgumentException("endTime must not be null");
        }

        if(!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

    }
}
