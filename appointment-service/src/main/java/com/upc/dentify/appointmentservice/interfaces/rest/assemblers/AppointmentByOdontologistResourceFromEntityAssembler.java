package com.upc.dentify.appointmentservice.interfaces.rest.assemblers;

import com.upc.dentify.appointmentservice.application.internal.outboundservices.ExternalPatientService;
import com.upc.dentify.appointmentservice.domain.model.aggregates.Appointment;
import com.upc.dentify.appointmentservice.interfaces.rest.dtos.AppointmentByOdontologistResource;

public class AppointmentByOdontologistResourceFromEntityAssembler {
    public static AppointmentByOdontologistResource fromEntityToResource(Appointment entity,
                                                                         ExternalPatientService externalPatientService) {
        var patient = externalPatientService.getPatientById(entity.getPatientId());
        return new AppointmentByOdontologistResource(
                entity.getId(),
                entity.getState().name(),
                patient.firstName(),
                patient.lastName(),
                entity.getShiftName(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getAppointmentDate(),
                entity.getAppointmentDate().getDayOfWeek().name(),
                entity.getClinicId()
        );
    }
}
