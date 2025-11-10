package com.upc.dentify.patientattentionservice.interfaces.rest;

import com.upc.dentify.patientattentionservice.domain.model.queries.GetClinicalRecordByPatientIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.ClinicalRecordQueryService;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.ClinicalRecordResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.ClinicalRecordResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Clinical Records", description = "Clinical Records Endpoint")
@RequestMapping(value ="/api/v1/clinical-records", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClinicalRecordController {
    private final ClinicalRecordQueryService clinicalRecordQueryService;

    public ClinicalRecordController(ClinicalRecordQueryService clinicalRecordQueryService) {
        this.clinicalRecordQueryService = clinicalRecordQueryService;
    }

    @PreAuthorize("hasAnyAuthority('ODONTOLOGIST', 'ADMIN')")
    @GetMapping("/patient-id/{patientId}")
    public ResponseEntity<ClinicalRecordResource> getClinicalRecordByPatientId(@PathVariable("patientId") Long patientId) {
        var query = new GetClinicalRecordByPatientIdQuery(patientId);
        return clinicalRecordQueryService.handle(query)
                .map(clinicalRecord -> ResponseEntity.ok(
                        ClinicalRecordResourceFromEntityAssembler.fromEntityToResource(clinicalRecord)
                )).orElse(ResponseEntity.notFound().build());
    }
}
