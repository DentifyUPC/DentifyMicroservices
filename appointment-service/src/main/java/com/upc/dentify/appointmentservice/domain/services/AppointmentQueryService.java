package com.upc.dentify.appointmentservice.domain.services;

import com.upc.dentify.appointmentservice.domain.model.aggregates.Appointment;
import com.upc.dentify.appointmentservice.domain.model.queries.GetAppointmentByIdQuery;
import com.upc.dentify.appointmentservice.domain.model.queries.GetAppointmentByOdontologistIdQuery;
import com.upc.dentify.appointmentservice.domain.model.queries.GetAppointmentByPatientIdQuery;

import java.util.List;
import java.util.Optional;

public interface AppointmentQueryService {
    List<Appointment> handle(GetAppointmentByPatientIdQuery query);
    List<Appointment> handle(GetAppointmentByOdontologistIdQuery query);
    Optional<Appointment> handle(GetAppointmentByIdQuery query);
}
