package com.upc.dentify.appointmentservice.interfaces.rest.assemblers;

import com.upc.dentify.appointmentservice.domain.model.command.CreateAppointmentCommand;
import com.upc.dentify.appointmentservice.interfaces.rest.dtos.CreateAppointmentResource;

public class CreateAppointmentCommandFromResourceAssembler {
    public static CreateAppointmentCommand fromResourceToCommand(CreateAppointmentResource resource) {
        return new CreateAppointmentCommand(
                resource.patientId(),
                resource.odontologistId(),
                resource.startTime(),
                resource.endTime(),
                resource.appointmentDate(),
                resource.shiftName(),
                resource.clinicId()
        );
    }
}
