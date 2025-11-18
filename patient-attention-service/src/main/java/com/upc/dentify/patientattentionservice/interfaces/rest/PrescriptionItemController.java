package com.upc.dentify.patientattentionservice.interfaces.rest;

import com.upc.dentify.patientattentionservice.domain.model.commands.CreatePrescriptionItemCommand;
import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllPrescriptionItemsByPrescriptionIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.PrescriptionItemCommandService;
import com.upc.dentify.patientattentionservice.domain.services.PrescriptionItemQueryService;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.CreatePrescriptionItemResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.PrescriptionItemResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.UpdatePrescriptionItemResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.CreatePrescriptionCommandFromResourceAssembler;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.PrescriptionItemResourceFromEntityAssembler;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.UpdatePrescriptionItemCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Prescription Items", description = "Prescription Items Endpoint")
@RequestMapping(value ="/api/v1/prescription-items", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrescriptionItemController {
    private final PrescriptionItemQueryService prescriptionItemQueryService;
    private final PrescriptionItemCommandService prescriptionItemCommandService;

    public PrescriptionItemController(PrescriptionItemQueryService prescriptionItemQueryService,
                                      PrescriptionItemCommandService prescriptionItemCommandService) {
        this.prescriptionItemQueryService = prescriptionItemQueryService;
        this.prescriptionItemCommandService = prescriptionItemCommandService;
    }

    @PreAuthorize("hasAuthority('ODONTOLOGIST')")
    @PutMapping("/{prescriptionItemId}")
    public ResponseEntity<PrescriptionItemResource> updatePrescriptionItem(@PathVariable("prescriptionItemId") Long prescriptionItemId,
                                                                           @RequestBody UpdatePrescriptionItemResource resource) {
        var command = UpdatePrescriptionItemCommandFromResourceAssembler.fromResourceToCommand(prescriptionItemId, resource);
        var result = prescriptionItemCommandService.handle(command);

        return result.map(prescriptionItems -> ResponseEntity.ok(
                PrescriptionItemResourceFromEntityAssembler.fromEntityToResource(prescriptionItems)
        )).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/prescription/{prescriptionId}")
    public List<PrescriptionItemResource> getAllPrescriptionItemsByPrescriptionId(@PathVariable("prescriptionId") Long prescriptionId) {
        var prescriptionItems = prescriptionItemQueryService.handle(new GetAllPrescriptionItemsByPrescriptionIdQuery(prescriptionId));
        return prescriptionItems.stream()
                .map(PrescriptionItemResourceFromEntityAssembler::fromEntityToResource)
                .toList();
    }

    @PreAuthorize("hasAuthority('ODONTOLOGIST')")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Prescription item created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createPrescriptionItem(@RequestBody CreatePrescriptionItemResource resource) {
        try {
            CreatePrescriptionItemCommand command =
                    CreatePrescriptionCommandFromResourceAssembler.fromResourceToCommand(resource);
            var prescriptionItem = prescriptionItemCommandService.handle(command);
            if (prescriptionItem.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Unable to create PrescriptionItem"));
            }

            var response = PrescriptionItemResourceFromEntityAssembler.fromEntityToResource(prescriptionItem.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + e.getMessage()));
        }
    }
}