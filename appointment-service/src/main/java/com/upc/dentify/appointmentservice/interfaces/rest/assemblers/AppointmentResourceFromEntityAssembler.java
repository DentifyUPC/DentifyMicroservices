package com.upc.dentify.appointmentservice.interfaces.rest.assemblers;

import com.upc.dentify.appointmentservice.domain.model.aggregates.Appointment;
import com.upc.dentify.appointmentservice.interfaces.rest.dtos.AppointmentResource;

public class AppointmentResourceFromEntityAssembler {
    public static AppointmentResource fromEntityToResource(Appointment entity) {
        return new AppointmentResource(
                entity.getId(),
                entity.getState().name(),
                entity.getPatientId(),
                entity.getOdontologistId(),
                entity.getShiftName(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getAppointmentDate(),
                entity.getAppointmentDate().getDayOfWeek().name(),
                entity.getClinicId()
        );
    }
}
