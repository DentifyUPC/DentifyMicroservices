package com.upc.dentify.servicecatalogservice.interfaces.rest;

import com.upc.dentify.servicecatalogservice.domain.model.queries.GetAllServicesQuery;
import com.upc.dentify.servicecatalogservice.domain.services.ServiceQueryService;
import com.upc.dentify.servicecatalogservice.interfaces.rest.assemblers.ServiceResourceFromEntityAssembler;
import com.upc.dentify.servicecatalogservice.interfaces.rest.dtos.ServiceResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/services", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Services", description = "Services Endpoint")
public class ServiceController {

    private final ServiceQueryService serviceQueryService;

    public ServiceController(ServiceQueryService serviceQueryService) {
        this.serviceQueryService = serviceQueryService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    @Operation(summary = "Get all services", description = "Get all services")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services found"),
            @ApiResponse(responseCode = "404", description = "Services not found")
    })
    public ResponseEntity<List<ServiceResource>> getAllServices() {
        var services = serviceQueryService.handle(new GetAllServicesQuery());

        if (services.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resources = services.stream()
                .map(ServiceResourceFromEntityAssembler:: toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources);
    }
}
