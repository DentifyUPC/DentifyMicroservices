package com.upc.dentify.appointmentservice.application.internal.queryservices;

import com.upc.dentify.appointmentservice.domain.model.aggregates.Appointment;
import com.upc.dentify.appointmentservice.domain.model.queries.GetAppointmentByOdontologistIdQuery;
import com.upc.dentify.appointmentservice.domain.model.queries.GetAppointmentByPatientIdQuery;
import com.upc.dentify.appointmentservice.domain.services.AppointmentQueryService;
import com.upc.dentify.appointmentservice.infrastructure.persistence.jpa.repositories.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentQueryServiceImpl implements AppointmentQueryService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentQueryServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<Appointment> handle(GetAppointmentByOdontologistIdQuery query) {
        return appointmentRepository.findAllByOdontologistId(query.odontologistId());
    }

    @Override
    public List<Appointment> handle(GetAppointmentByPatientIdQuery query) {
        return appointmentRepository.findAllByPatientId(query.patientId());
    }
}