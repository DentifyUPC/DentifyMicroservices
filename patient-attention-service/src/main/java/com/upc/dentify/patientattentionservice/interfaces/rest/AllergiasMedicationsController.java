package com.upc.dentify.patientattentionservice.interfaces.rest;

import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllergiasMedicationsByIdQuery;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllergiasMedicationsByClinicalRecordIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.AllergiasMedicationsCommandService;
import com.upc.dentify.patientattentionservice.domain.services.AllergiasMedicationsQueryService;
import com.upc.dentify.patientattentionservice.infrastructure.persistence.jpa.repositories.AllergiasMedicationsRepository;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.AllergiasMedicationsResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.CreateAllergiasMedicationsResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.UpdateAllergiasMedicationsResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.AllergiasMedicationsResourceFromEntityAssembler;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.CreateAllergiasMedicationsCommandFromResourceAssembler;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.UpdateAllergiasMedicationsCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Allergias Medications", description = "Allergias Medications Endpoint")
@RequestMapping(value = "/api/v1/allergias-medications", produces = MediaType.APPLICATION_JSON_VALUE)
public class AllergiasMedicationsController {
    private final AllergiasMedicationsCommandService allergiasMedicationsCommandService;
    private final AllergiasMedicationsQueryService allergiasMedicationsQueryService;
    private final AllergiasMedicationsRepository allergiasMedicationsRepository;

    public AllergiasMedicationsController(AllergiasMedicationsCommandService allergiasMedicationsCommandService,
                                          AllergiasMedicationsQueryService allergiasMedicationsQueryService,
                                          AllergiasMedicationsRepository allergiasMedicationsRepository) {
        this.allergiasMedicationsCommandService = allergiasMedicationsCommandService;
        this.allergiasMedicationsQueryService = allergiasMedicationsQueryService;
        this.allergiasMedicationsRepository = allergiasMedicationsRepository;
    }

    @PreAuthorize("hasAuthority('ODONTOLOGIST')")
    @PostMapping
    public ResponseEntity<AllergiasMedicationsResource> createAllergiasMedications(
            @RequestBody CreateAllergiasMedicationsResource resource) {
        var command = CreateAllergiasMedicationsCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = allergiasMedicationsCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AllergiasMedicationsResourceFromEntityAssembler.toResourceFromEntity(result));
    }

    @PreAuthorize("hasAnyAuthority('ODONTOLOGIST', 'ADMIN')")
    @GetMapping("/{allergiaMedicationId}")
    public ResponseEntity<AllergiasMedicationsResource> getAllergiasMedicationById(@PathVariable Long allergiaMedicationId) {
        var query = new GetAllergiasMedicationsByIdQuery(allergiaMedicationId);
        return allergiasMedicationsQueryService.handle(query)
                .map(allergiasMedication -> ResponseEntity.ok(
                        AllergiasMedicationsResourceFromEntityAssembler.toResourceFromEntity(allergiasMedication)
                )).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('ODONTOLOGIST', 'ADMIN')")
    @GetMapping("/clinical-record/{clinicalRecordId}")
    public ResponseEntity<List<AllergiasMedicationsResource>> getAllergiasMedicationsByClinicalRecordId(
            @PathVariable Long clinicalRecordId) {
        var query = new GetAllergiasMedicationsByClinicalRecordIdQuery(clinicalRecordId);
        var allergiasMedications = allergiasMedicationsQueryService.handle(query);
        var resources = allergiasMedications.stream()
                .map(AllergiasMedicationsResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @PreAuthorize("hasAuthority('ODONTOLOGIST')")
    @PutMapping("/{allergiaMedicationId}")
    public ResponseEntity<AllergiasMedicationsResource> updateAllergiasMedications(
            @PathVariable Long allergiaMedicationId,
            @RequestBody UpdateAllergiasMedicationsResource resource) {
        var command = UpdateAllergiasMedicationsCommandFromResourceAssembler.toCommandFromResource(allergiaMedicationId, resource);
        var result = allergiasMedicationsCommandService.handle(command);
        return result.map(allergiasMedication -> ResponseEntity.ok(
                AllergiasMedicationsResourceFromEntityAssembler.toResourceFromEntity(allergiasMedication)
        )).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('ODONTOLOGIST')")
    @DeleteMapping("/{allergiaMedicationId}")
    public ResponseEntity<Void> deleteAllergiasMedications(@PathVariable Long allergiaMedicationId) {
        if (allergiasMedicationsRepository.existsById(allergiaMedicationId)) {
            allergiasMedicationsRepository.deleteById(allergiaMedicationId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
