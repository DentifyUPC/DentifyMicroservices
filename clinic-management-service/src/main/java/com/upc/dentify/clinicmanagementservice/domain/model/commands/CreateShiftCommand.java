package com.upc.dentify.clinicmanagementservice.domain.model.commands;

import com.upc.dentify.clinicmanagementservice.domain.model.valueobjects.ShiftName;

import java.time.LocalTime;
import java.util.Arrays;

public record CreateShiftCommand(LocalTime startTime, LocalTime endTime, String name, Long clinicId) {

    public CreateShiftCommand {

        if (clinicId == null || clinicId <= 0) {
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

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be null or blank");
        }

        boolean isValid = Arrays.stream(ShiftName.values())
                .anyMatch(shiftName -> shiftName.name().equalsIgnoreCase(name));

        if (!isValid) {
            throw new IllegalArgumentException("Invalid shift name. Allowed values are: "
                    + Arrays.toString(ShiftName.values()));
        }

    }

}
