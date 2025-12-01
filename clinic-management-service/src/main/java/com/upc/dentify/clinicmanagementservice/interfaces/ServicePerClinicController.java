package com.upc.dentify.clinicmanagementservice.interfaces;

import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateServicePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetAllServicesPerClinicsQuery;
import com.upc.dentify.clinicmanagementservice.domain.services.ServicePerClinicCommandService;
import com.upc.dentify.clinicmanagementservice.domain.services.ServicePerClinicQueryService;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.assemblers.*;
import com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos.*;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping(value = "api/v1/services-per-clinics", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Services per clinics", description = "Services per Clinics Endpoint")
public class ServicePerClinicController {
    private final ServicePerClinicCommandService servicePerClinicCommandService;
    private final ServicePerClinicQueryService servicePerClinicQueryService;

    public ServicePerClinicController(ServicePerClinicCommandService servicePerClinicCommandService,
                                      ServicePerClinicQueryService servicePerClinicQueryService) {
        this.servicePerClinicCommandService = servicePerClinicCommandService;
        this.servicePerClinicQueryService = servicePerClinicQueryService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(summary = "Create service per clinic", description = "Create a new service per clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Clinic per clinic created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Clinic already exists in clinic"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<?> createServicePerClinic(@RequestBody CreateServicePerClinicResource resource) {
        try {
            var command = CreateServicePerClinicCommandFromResourceAssembler.toCommandFromResource(resource);
            var entity = servicePerClinicCommandService.handle(command);
            if (entity.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Unable to create service per clinic"));
            }
            var response = ServicePerClinicResourceFromEntityAssembler.toResourceFromEntity(entity.get());
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update service per clinic", description = "Update a service per clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated service per clinic"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<ServicePerClinicResource> updateServicePerClinic(@PathVariable Long id,
                                                                  @RequestBody UpdateServicePerClinicResource resource) {

        UpdateServicePerClinicCommand command = UpdateServicePerClinicCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var service = servicePerClinicCommandService.handle(command);

        if(service.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var serviceResource = ServicePerClinicResourceFromEntityAssembler.toResourceFromEntity(service.get());

        return ResponseEntity.ok(serviceResource);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PATIENT')")
    @GetMapping("/{clinicId}")
    @Operation(summary = "Get all services by clinic ID", description = "Get all services by clinic ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services found"),
            @ApiResponse(responseCode = "404", description = "Services not found")
    })
    public ResponseEntity<List<ServiceFormatResource>> getAllServicePerClinics(@PathVariable Long clinicId) {
        var services = servicePerClinicQueryService.handle(new GetAllServicesPerClinicsQuery(clinicId));
        if(services.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(services);
    }
}
