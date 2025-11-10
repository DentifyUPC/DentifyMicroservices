package com.upc.dentify.patientattentionservice.interfaces.rest;

import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllClinicalRecordEntriesByClinicalRecordIdQuery;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetClinicalRecordEntryByIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.ClinicalRecordEntriesCommandService;
import com.upc.dentify.patientattentionservice.domain.services.ClinicalRecordEntryQueryService;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.ClinicalRecordEntryResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.UpdateClinicalRecordEntryResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.ClinicalRecordEntryResourceFromEntityAssembler;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.UpdateClinicalRecordEntryCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Clinical Record Entries", description = "Clinical Record Entries Endpoint")
@RequestMapping(value ="/api/v1/clinical-record-entries", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClinicalRecordEntryController {
    private final ClinicalRecordEntriesCommandService clinicalRecordEntriesCommandService;
    private final ClinicalRecordEntryQueryService clinicalRecordEntryQueryService;

    public ClinicalRecordEntryController(ClinicalRecordEntriesCommandService clinicalRecordEntriesCommandService,
                                         ClinicalRecordEntryQueryService clinicalRecordEntryQueryService) {
        this.clinicalRecordEntriesCommandService = clinicalRecordEntriesCommandService;
        this.clinicalRecordEntryQueryService = clinicalRecordEntryQueryService;
    }

    @PreAuthorize("hasAnyAuthority('ODONTOLOGIST', 'ADMIN')")
    @GetMapping("/clinical-record-id/{clinicalRecordId}")
    public List<ClinicalRecordEntryResource> getAllClinicalRecordEntriesByClinicalRecordId(@PathVariable("clinicalRecordId") Long clinicalRecordId) {
        var clinical = clinicalRecordEntryQueryService.handle(new GetAllClinicalRecordEntriesByClinicalRecordIdQuery(clinicalRecordId));
        return clinical.stream()
                .map(ClinicalRecordEntryResourceFromEntityAssembler::fromEntityToResource)
                .toList();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'ODONTOLOGIST')")
    @GetMapping("/{clinicalRecordEntryId}")
    public ResponseEntity<ClinicalRecordEntryResource> getClinicalRecordEntryById(@PathVariable("clinicalRecordEntryId") Long clinicalRecordEntryId) {
        var query = new GetClinicalRecordEntryByIdQuery(clinicalRecordEntryId);
        return clinicalRecordEntryQueryService.handle(query)
                .map(clinical -> ResponseEntity.ok(
                        ClinicalRecordEntryResourceFromEntityAssembler.fromEntityToResource(clinical)
                )).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('ODONTOLOGIST')")
    @PutMapping("/{clinicalRecordEntryId}")
    public ResponseEntity<ClinicalRecordEntryResource> updateClinicalRecordEntry(@PathVariable("clinicalRecordEntryId") Long clinicalRecordEntryId,
                                                                            @RequestBody UpdateClinicalRecordEntryResource resource) {
        var command = UpdateClinicalRecordEntryCommandFromResourceAssembler.fromResourceToCommand(clinicalRecordEntryId, resource);
        var result = clinicalRecordEntriesCommandService.handle(command);
        return result.map(clinical -> ResponseEntity.ok(
                ClinicalRecordEntryResourceFromEntityAssembler.fromEntityToResource(clinical)
        )).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
