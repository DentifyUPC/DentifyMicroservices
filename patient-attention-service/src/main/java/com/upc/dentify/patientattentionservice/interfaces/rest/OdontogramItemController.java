package com.upc.dentify.patientattentionservice.interfaces.rest;

import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllOdontogramItemsByOdontogramIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.OdontogramItemCommandService;
import com.upc.dentify.patientattentionservice.domain.services.OdontogramItemQueryService;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.*;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Odontogram Item", description = "Odontogram Item Endpoint")
@RequestMapping(value ="/api/v1/odontogram-item", produces = MediaType.APPLICATION_JSON_VALUE)
public class OdontogramItemController {

    private final OdontogramItemCommandService odontogramItemCommandService;
    private final OdontogramItemQueryService odontogramItemQueryService;

    public OdontogramItemController(OdontogramItemCommandService odontogramItemCommandService,
                                    OdontogramItemQueryService odontogramItemQueryService) {
        this.odontogramItemCommandService = odontogramItemCommandService;
        this.odontogramItemQueryService = odontogramItemQueryService;
    }

    @PreAuthorize("hasAnyAuthority('ODONTOLOGIST', 'ADMIN')")
    @GetMapping("/odontogram-id/{odontogramId}")
    public List<OdontogramItemResource> getAllOdontogramItemsByOdontogramId(@PathVariable("odontogramId") Long odontogramId) {
        var odontogramItems = odontogramItemQueryService.handle(new GetAllOdontogramItemsByOdontogramIdQuery(odontogramId));
        return odontogramItems.stream()
                .map(OdontogramItemResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }

    @PreAuthorize("hasAnyAuthority('ODONTOLOGIST', 'ADMIN')")
    @PutMapping("/{odontogramItemId}")
    public ResponseEntity<OdontogramItemResource> updatePatient(@PathVariable("odontogramItemId") Long odontogramItemId, @RequestBody UpdateOdontogramItemResource requestResource) {
        var command = UpdateOdontogramItemCommandFromResourceAssembler.toCommandFromResource(odontogramItemId, requestResource);
        var result = odontogramItemCommandService.handle(command);

        return result.map(odontogramItem -> ResponseEntity.ok(
                OdontogramItemResourceFromEntityAssembler.toResourceFromEntity(odontogramItem)
        )).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
