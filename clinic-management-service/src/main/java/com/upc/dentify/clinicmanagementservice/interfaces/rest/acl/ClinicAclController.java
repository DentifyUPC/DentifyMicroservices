package com.upc.dentify.clinicmanagementservice.interfaces.rest.acl;

import com.upc.dentify.clinicmanagementservice.interfaces.acl.ClinicContextFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/acl/clinic")
public class ClinicAclController {
    private final ClinicContextFacade clinicContextFacade;

    public ClinicAclController(ClinicContextFacade clinicContextFacade) {
        this.clinicContextFacade = clinicContextFacade;
    }

    @GetMapping("/{clinicId}")
    ResponseEntity<Boolean> existsByClinicId(@PathVariable Long clinicId) {
        boolean exists = clinicContextFacade.existsByClinicId(clinicId);
        return ResponseEntity.ok(exists);
    }
}
