package com.upc.dentify.appointmentservice.interfaces.rest.assemblers;

import com.upc.dentify.appointmentservice.domain.model.command.UpdateAppointmentCommand;
import com.upc.dentify.appointmentservice.interfaces.rest.dtos.UpdateAppointmentResource;

public class UpdateAppointmentCommandFromResourceAssembler {
    public static UpdateAppointmentCommand fromResourceToCommand(Long id, UpdateAppointmentResource resource) {
        return new UpdateAppointmentCommand(id, resource.state());
    }
}
