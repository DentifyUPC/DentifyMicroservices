package com.upc.dentify.appointmentservice.interfaces.rest;

import com.upc.dentify.appointmentservice.application.internal.outboundservices.ExternalOdontologistService;
import com.upc.dentify.appointmentservice.application.internal.outboundservices.ExternalPatientService;
import com.upc.dentify.appointmentservice.domain.model.command.CreateAppointmentCommand;
import com.upc.dentify.appointmentservice.domain.model.command.UpdateAppointmentCommand;
import com.upc.dentify.appointmentservice.domain.model.queries.GetAppointmentByOdontologistIdQuery;
import com.upc.dentify.appointmentservice.domain.model.queries.GetAppointmentByPatientIdQuery;
import com.upc.dentify.appointmentservice.domain.services.AppointmentCommandService;
import com.upc.dentify.appointmentservice.domain.services.AppointmentQueryService;
import com.upc.dentify.appointmentservice.interfaces.rest.assemblers.*;
import com.upc.dentify.appointmentservice.interfaces.rest.dtos.*;
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
@RequestMapping(value = "api/v1/appointment", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Appointment", description = "Appointment Endpoint")
public class AppointmentController {
    private final AppointmentCommandService appointmentCommandService;
    private final AppointmentQueryService appointmentQueryService;
    private final ExternalOdontologistService externalOdontologistService;
    private final ExternalPatientService externalPatientService;

    public AppointmentController(AppointmentCommandService appointmentCommandService, AppointmentQueryService appointmentQueryService,
                                 ExternalOdontologistService externalOdontologistService, ExternalPatientService externalPatientService) {
        this.appointmentCommandService = appointmentCommandService;
        this.appointmentQueryService = appointmentQueryService;
        this.externalOdontologistService = externalOdontologistService;
        this.externalPatientService = externalPatientService;
    }

    @PreAuthorize("hasAuthority('PATIENT')")
    @PostMapping
    @Operation(summary = "Create appointment", description = "Create a new appointment in the clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Appointment already exists in clinic"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createAppointment(@RequestBody CreateAppointmentResource resource) {
        try {
            CreateAppointmentCommand command = CreateAppointmentCommandFromResourceAssembler.fromResourceToCommand(resource);

            var appointment = appointmentCommandService.handle(command);

            if (appointment.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Appointment not created"));
            }

            var response = AppointmentResourceFromEntityAssembler.fromEntityToResource(appointment.get());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error: " + e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('PATIENT')")
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get all appointments by patient id", description = "Get all appointments by patient id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments found"),
            @ApiResponse(responseCode = "404", description = "Appointments not found")
    })
    public ResponseEntity<List<AppointmentByPatientResource>> getAllAppointmentsByPatient(@PathVariable Long patientId) {
        var appointments = appointmentQueryService.handle(new GetAppointmentByPatientIdQuery(patientId));
        if (appointments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var appointmentResources = appointments.stream()
                .map(a -> AppointmentByPatientResourceFromEntityAssembler.fromEntityToResource(a, externalOdontologistService))
                .toList();
        return ResponseEntity.ok(appointmentResources);
    }

    @PreAuthorize("hasAuthority('ODONTOLOGIST')")
    @GetMapping("/odontologist/{odontologistId}")
    @Operation(summary = "Get all appointments by odontologist id", description = "Get all appointments by odontologist id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments found"),
            @ApiResponse(responseCode = "404", description = "Appointments not found")
    })
    public ResponseEntity<List<AppointmentByOdontologistResource>> getAllAppointmentsByOdontologist(@PathVariable Long odontologistId) {
        var appointments = appointmentQueryService.handle(new GetAppointmentByOdontologistIdQuery(odontologistId));
        if (appointments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var appointmentResources = appointments.stream()
                .map(a -> AppointmentByOdontologistResourceFromEntityAssembler.fromEntityToResource(a, externalPatientService))
                .toList();
        return ResponseEntity.ok(appointmentResources);
    }

    @PreAuthorize("hasAuthority('ODONTOLOGIST')")
    @PutMapping("/{id}")
    @Operation(summary = "Update appointment", description = "Update appointment using the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated appointment"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<AppointmentResource> updateAppointment(@PathVariable Long id,
                                                                 @RequestBody UpdateAppointmentResource resource) {
        UpdateAppointmentCommand command = UpdateAppointmentCommandFromResourceAssembler.fromResourceToCommand(id, resource);
        var appointment = appointmentCommandService.handle(command);

        if (appointment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var response = AppointmentResourceFromEntityAssembler.fromEntityToResource(appointment.get());

        return ResponseEntity.ok(response);
    }
}