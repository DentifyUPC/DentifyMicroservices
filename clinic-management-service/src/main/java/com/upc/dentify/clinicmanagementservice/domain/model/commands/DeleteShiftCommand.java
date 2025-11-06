package com.upc.dentify.clinicmanagementservice.domain.model.commands;

public record DeleteShiftCommand(Long shiftId) {

    public DeleteShiftCommand {

        if(shiftId == null || shiftId <= 0) {
            throw new IllegalArgumentException("Shift id must be greater than 0");
        }

    }
}
