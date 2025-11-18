package com.upc.dentify.appointmentservice.interfaces.rest.acl;

import com.upc.dentify.appointmentservice.interfaces.acl.AppointmentContextFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/acl/appointment")
public class AppointmentAclController {
    private final AppointmentContextFacade appointmentContextFacade;

    public AppointmentAclController(AppointmentContextFacade appointmentContextFacade) {
        this.appointmentContextFacade = appointmentContextFacade;
    }

    @GetMapping("/{appointmentId}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long appointmentId) {
        boolean exists = appointmentContextFacade.existsById(appointmentId);
        return ResponseEntity.ok(exists);
    }
}
