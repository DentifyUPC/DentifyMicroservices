package com.upc.dentify.clinicmanagementservice.interfaces.rest;

import com.upc.dentify.clinicmanagementservice.domain.model.commands.CreateSchedulePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.commands.UpdateSchedulePerClinicCommand;
import com.upc.dentify.clinicmanagementservice.domain.model.queries.GetSchedulePerClinicByClinicIdQuery;
import com.upc.dentify.clinicmanagementservice.domain.services.SchedulePerClinicCommandService;
import com.upc.dentify.clinicmanagementservice.domain.services.SchedulePerClinicQueryService;
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

import java.util.Map;

@RestController
@RequestMapping(value = "api/v1/schedule-per-clinic", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Schedule per clinic", description = "Schedule per clinic Endpoint")
public class SchedulePerClinicController {

    private final SchedulePerClinicQueryService schedulePerClinicQueryService;
    private final SchedulePerClinicCommandService schedulePerClinicCommandService;

    public SchedulePerClinicController(SchedulePerClinicQueryService schedulePerClinicQueryService,
                                       SchedulePerClinicCommandService schedulePerClinicCommandService) {
        this.schedulePerClinicQueryService = schedulePerClinicQueryService;
        this.schedulePerClinicCommandService = schedulePerClinicCommandService;
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(summary = "Create schedule per clinic", description = "Create a new schedule per clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Schedule per clinic created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Schedule already exists in clinic"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createServicePerClinic(@RequestBody CreateSchedulePerClinicResource resource) {
        try {
            CreateSchedulePerClinicCommand command =
                    CreateSchedulePerClinicCommandFromResourceAssembler.toCommandFromResource(resource);

            var schedulePerClinic = schedulePerClinicCommandService.handle(command);

            if (schedulePerClinic.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Unable to create SchedulePerClinic"));
            }

            var response = SchedulePerClinicResourceFromEntityAssembler.toResourceFromEntity(schedulePerClinic.get());

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
    @PutMapping("/{clinicId}")
    @Operation(summary = "Update schedule per clinic", description = "Update schedule per clinic using the clinic id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated schedule per clinic"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<SchedulePerClinicResource> updateSchedulePerClinic(@PathVariable Long clinicId,
                                                               @RequestBody UpdateSchedulePerClinicResource resource) {

        UpdateSchedulePerClinicCommand command = UpdateSchedulePerClinicCommandFromResourceAssembler.toCommandFromResource(clinicId, resource);
        var schedulePerClinic = schedulePerClinicCommandService.handle(command);

        if(schedulePerClinic.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var schedulePerClinicResource = SchedulePerClinicResourceFromEntityAssembler.toResourceFromEntity(schedulePerClinic.get());

        return ResponseEntity.ok(schedulePerClinicResource);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{clinicId}")
    @Operation(summary = "Get schedule by clinic id", description = "Get schedule by clinic id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule per clinic found"),
            @ApiResponse(responseCode = "404", description = "Schedule per clinic not found")
    })
    public ResponseEntity<SchedulePerClinicResource> getSchedulePerClinic(@PathVariable Long clinicId) {
        var schedulePerClinic = schedulePerClinicQueryService.handle(new GetSchedulePerClinicByClinicIdQuery(clinicId));

        if (schedulePerClinic.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var schedulePerClinicResource = SchedulePerClinicResourceFromEntityAssembler.toResourceFromEntity(schedulePerClinic.get());

        return ResponseEntity.ok(schedulePerClinicResource);
    }


}
