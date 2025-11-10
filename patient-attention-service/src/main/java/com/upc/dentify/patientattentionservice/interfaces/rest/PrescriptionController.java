package com.upc.dentify.patientattentionservice.interfaces.rest;

import com.upc.dentify.patientattentionservice.domain.model.queries.GetPrescriptionByIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.PrescriptionCommandService;
import com.upc.dentify.patientattentionservice.domain.services.PrescriptionQueryService;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.PrescriptionResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.UpdatePrescriptionResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.PrescriptionResourceFromEntityAssembler;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.UpdatePrescriptionCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Prescription", description = "Prescription Endpoint")
@RequestMapping(value ="/api/v1/prescriptions", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrescriptionController {
    private final PrescriptionQueryService prescriptionQueryService;
    private final PrescriptionCommandService prescriptionCommandService;

    public PrescriptionController(PrescriptionQueryService prescriptionQueryService, PrescriptionCommandService prescriptionCommandService) {
        this.prescriptionQueryService = prescriptionQueryService;
        this.prescriptionCommandService = prescriptionCommandService;
    }

    @PreAuthorize("hasAuthority('ODONTOLOGIST')")
    @PutMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionResource> updatePrescription(@PathVariable("prescriptionId") Long prescriptionId,
                                                                   @RequestBody UpdatePrescriptionResource resource) {
        var command = UpdatePrescriptionCommandFromResourceAssembler.fromResourceToCommand(prescriptionId, resource);
        var result = prescriptionCommandService.handle(command);

        return result.map(prescription -> ResponseEntity.ok(
                PrescriptionResourceFromEntityAssembler.fromEntityToResource(prescription)
        )).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('ODONTOLOGIST', 'ADMIN')")
    @GetMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionResource> getPrescriptionById(@PathVariable("prescriptionId") Long prescriptionId) {
        var query = new GetPrescriptionByIdQuery(prescriptionId);
        return prescriptionQueryService.handle(query)
                .map(prescription -> ResponseEntity.ok(
                        PrescriptionResourceFromEntityAssembler.fromEntityToResource(prescription)
                )).orElse(ResponseEntity.notFound().build());
    }
}
