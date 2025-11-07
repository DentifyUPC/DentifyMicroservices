package com.upc.dentify.patientattentionservice.interfaces.rest.acl;

import com.upc.dentify.patientattentionservice.interfaces.acl.PatientContextFacade;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.PatientExternalResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/acl/patient")
public class PatientAclController {
    private final PatientContextFacade patientContextFacade;

    public PatientAclController(PatientContextFacade patientContextFacade) {
        this.patientContextFacade = patientContextFacade;
    }

    @GetMapping("/clinic/{clinicId}")
    ResponseEntity<List<PatientExternalResource>> getAllPatientsByClinicId(@PathVariable Long clinicId) {
        List<PatientExternalResource> patients = patientContextFacade.getAllPatientsByClinicId(clinicId);
        if (patients.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    ResponseEntity<PatientExternalResource> getPatientById(@PathVariable Long id) {
        return patientContextFacade.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
