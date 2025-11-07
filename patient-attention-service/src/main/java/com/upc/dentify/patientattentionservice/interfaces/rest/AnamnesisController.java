package com.upc.dentify.patientattentionservice.interfaces.rest;

import com.upc.dentify.patientattentionservice.domain.model.queries.GetAnamnesisByIdQuery;
import com.upc.dentify.patientattentionservice.domain.services.AnamnesisCommandService;
import com.upc.dentify.patientattentionservice.domain.services.AnamnesisQueryService;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.AnamnesisResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.UpdateAnamnesisResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.AnamnesisResourceFromEntityAssembler;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.UpdateAnamnesisCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Anamnesis", description = "Anamnesis Endpoint")
@RequestMapping(value ="/api/v1/anamnesis", produces = MediaType.APPLICATION_JSON_VALUE)
public class AnamnesisController {
    private final AnamnesisCommandService anamnesisCommandService;
    private final AnamnesisQueryService anamnesisQueryService;

    public AnamnesisController(AnamnesisCommandService anamnesisCommandService, AnamnesisQueryService anamnesisQueryService) {
        this.anamnesisCommandService = anamnesisCommandService;
        this.anamnesisQueryService = anamnesisQueryService;
    }

    @PreAuthorize("hasAuthority('ODONTOLOGIST')")
    @PutMapping("/{anamnesisId}")
    public ResponseEntity<AnamnesisResource> updateAnamnesis(@PathVariable Long anamnesisId,
                                                             @RequestBody UpdateAnamnesisResource resource) {
        var command = UpdateAnamnesisCommandFromResourceAssembler.toCommandFromResource(anamnesisId, resource);
        var result = anamnesisCommandService.handle(command);

        return result.map(anamnesis -> ResponseEntity.ok(
                AnamnesisResourceFromEntityAssembler.toResourceFromEntity(anamnesis)
        )).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('ODONTOLOGIST', 'ADMIN')")
    @GetMapping("/{anamnesisId}")
    public ResponseEntity<AnamnesisResource> getAnamnesisById(@PathVariable Long anamnesisId) {
        var query = new GetAnamnesisByIdQuery(anamnesisId);
        return anamnesisQueryService.handle(query)
                .map(anamnesis -> ResponseEntity.ok(
                        AnamnesisResourceFromEntityAssembler.toResourceFromEntity(anamnesis)
                )).orElse(ResponseEntity.notFound().build());
    }
}
