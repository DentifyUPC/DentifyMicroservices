package com.upc.dentify.clinicmanagementservice.domain.services;

import com.upc.dentify.clinicmanagementservice.domain.model.aggregates.SchedulePerClinic;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateSchedulePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateSchedulePerClinicCommand;

import java.util.Optional;

public interface SchedulePerClinicCommandService {
    Optional<SchedulePerClinic> handle(CreateSchedulePerClinicCommand command);
    Optional<SchedulePerClinic> handle(UpdateSchedulePerClinicCommand command);
}
