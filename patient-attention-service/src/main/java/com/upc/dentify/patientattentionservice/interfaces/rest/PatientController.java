package com.upc.dentify.patientattentionservice.interfaces.rest;

import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllPatientsByClinicId;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetPatientById;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetPatientByUserId;
import com.upc.dentify.patientattentionservice.domain.services.PatientCommandService;
import com.upc.dentify.patientattentionservice.domain.services.PatientQueryService;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.PatientResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.UpdatePatientRequestResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.PatientCommandFromResourceAssembler;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.PatientResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Patient", description = "Patient Endpoint")
@RequestMapping(value ="/api/v1/patients", produces = MediaType.APPLICATION_JSON_VALUE)
public class PatientController {
    private final PatientCommandService patientCommandService;
    private final PatientQueryService patientQueryService;

    public PatientController(PatientCommandService patientCommandService, PatientQueryService patientQueryService) {
        this.patientCommandService = patientCommandService;
        this.patientQueryService = patientQueryService;
    }

    @PreAuthorize("hasAuthority('PATIENT')")
    @PutMapping("/{patientId}")
    public ResponseEntity<PatientResource> updatePatient(@PathVariable("patientId") Long patientId, @RequestBody UpdatePatientRequestResource requestResource) {
        var command = PatientCommandFromResourceAssembler.toCommand(patientId, requestResource);
        var result = patientCommandService.handle(command);

        return result.map(patient -> ResponseEntity.ok(
                PatientResourceFromEntityAssembler.fromEntityToResource(patient)
        )).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/clinics/{clinicId}/patients")
    public List<PatientResource> getAllPatientsByClinicId(@PathVariable("clinicId") Long clinicId) {
        var patients = patientQueryService.handle(new GetAllPatientsByClinicId(clinicId));
        return patients.stream()
                .map(PatientResourceFromEntityAssembler::fromEntityToResource)
                .toList();
    }

    @PreAuthorize("hasAnyAuthority('ODONTOLOGIST', 'PATIENT')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<PatientResource> getPatientByUserId(@PathVariable("userId") Long userId) {
        var query = new GetPatientByUserId(userId);
        return patientQueryService.handle(query)
                .map(patient -> ResponseEntity.ok(
                        PatientResourceFromEntityAssembler.fromEntityToResource(patient)
                )).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResource> getPatientById(@PathVariable("patientId") Long patientId) {
        var query = new GetPatientById(patientId);
        return patientQueryService.handle(query)
                .map(patient -> ResponseEntity.ok(
                        PatientResourceFromEntityAssembler.fromEntityToResource(patient)
                )).orElse(ResponseEntity.notFound().build());
    }
}
