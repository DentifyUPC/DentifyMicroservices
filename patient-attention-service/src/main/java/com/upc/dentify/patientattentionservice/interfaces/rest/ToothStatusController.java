package com.upc.dentify.patientattentionservice.interfaces.rest;

import com.upc.dentify.patientattentionservice.domain.model.queries.GetAllToothStatusQuery;
import com.upc.dentify.patientattentionservice.domain.services.ToothStatusQueryService;
import com.upc.dentify.patientattentionservice.interfaces.rest.resources.ToothStatusResource;
import com.upc.dentify.patientattentionservice.interfaces.rest.transform.ToothStatusFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Tooth Status", description = "Tooth Status Endpoint")
@RequestMapping(value ="/api/v1/tooth-status", produces = MediaType.APPLICATION_JSON_VALUE)
public class ToothStatusController {

    private final ToothStatusQueryService toothStatusQueryService;

    public ToothStatusController(ToothStatusQueryService toothStatusQueryService) {
        this.toothStatusQueryService = toothStatusQueryService;
    }

    @PreAuthorize("hasAnyAuthority('ODONTOLOGIST', 'ADMIN')")
    @GetMapping
    public List<ToothStatusResource> getAllToothStatus() {
        var toothStatus = toothStatusQueryService.handle(new GetAllToothStatusQuery());
        return toothStatus.stream()
                .map(ToothStatusFromEntityAssembler::toResourceFromEntity)
                .toList();
    }
}
