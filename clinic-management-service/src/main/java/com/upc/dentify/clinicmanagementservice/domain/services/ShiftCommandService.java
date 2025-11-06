package com.upc.dentify.clinicmanagementservice.domain.services;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.Shift;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateShiftCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.DeleteShiftCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateShiftCommand;

import java.util.Optional;

public interface ShiftCommandService {
    Optional<Shift> handle(CreateShiftCommand command);
    Optional<Shift> handle(UpdateShiftCommand command);
    void handle(DeleteShiftCommand command);
}
