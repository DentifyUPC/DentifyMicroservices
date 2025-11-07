package com.upc.dentify.appointmentservice.domain.services;

import com.upc.dentify.appointmentservice.domain.model.aggregates.Appointment;
import com.upc.dentify.appointmentservice.domain.model.command.CreateAppointmentCommand;
import com.upc.dentify.appointmentservice.domain.model.command.UpdateAppointmentCommand;

import java.util.Optional;

public interface AppointmentCommandService {
    Optional<Appointment> handle(CreateAppointmentCommand command);
    Optional<Appointment> handle(UpdateAppointmentCommand command);
}
