package com.upc.dentify.appointmentservice.application.acl;

import com.upc.dentify.appointmentservice.domain.model.queries.GetAppointmentByIdQuery;
import com.upc.dentify.appointmentservice.domain.services.AppointmentQueryService;
import com.upc.dentify.appointmentservice.interfaces.acl.AppointmentContextFacade;
import org.springframework.stereotype.Service;

@Service
public class AppointmentContextFacadeImpl implements AppointmentContextFacade {
    private final AppointmentQueryService appointmentQueryService;

    public AppointmentContextFacadeImpl(AppointmentQueryService appointmentQueryService) {
        this.appointmentQueryService = appointmentQueryService;
    }

    @Override
    public Boolean existsById(Long id) {
        var appointment = appointmentQueryService.handle(new GetAppointmentByIdQuery(id));
        return appointment.isPresent();
    }
}
