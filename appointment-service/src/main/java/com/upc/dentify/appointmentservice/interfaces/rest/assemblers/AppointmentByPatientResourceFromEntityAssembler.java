package com.upc.dentify.appointmentservice.interfaces.rest.assemblers;

import com.upc.dentify.appointmentservice.application.internal.outboundservices.ExternalOdontologistService;
import com.upc.dentify.appointmentservice.domain.model.aggregates.Appointment;
import com.upc.dentify.appointmentservice.interfaces.rest.dtos.AppointmentByPatientResource;

public class AppointmentByPatientResourceFromEntityAssembler {
    public static AppointmentByPatientResource fromEntityToResource(Appointment entity,
                                                                    ExternalOdontologistService externalOdontologistService) {
        var odontologist = externalOdontologistService.getOdontologistById(entity.getOdontologistId());
        return new AppointmentByPatientResource(
                entity.getId(),
                entity.getState().name(),
                odontologist.firstName(),
                odontologist.lastName(),
                entity.getShiftName(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getAppointmentDate(),
                entity.getAppointmentDate().getDayOfWeek().name(),
                entity.getClinicId()
        );
    }
}